package org.estatio.capex.dom.bankaccount.documents;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.assertj.core.util.Lists;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;

import org.incode.module.document.dom.impl.docs.Document;
import org.incode.module.document.dom.impl.paperclips.Paperclip;
import org.incode.module.document.dom.impl.paperclips.PaperclipRepository;

import org.estatio.capex.dom.documents.LookupAttachedPdfService;
import org.estatio.capex.dom.invoice.IncomingInvoice;
import org.estatio.capex.dom.invoice.IncomingInvoiceRepository;
import org.estatio.dom.financial.bankaccount.BankAccount;
import org.estatio.dom.invoice.DocumentTypeData;

@Mixin(method="act")
public class BankAccount_attachInvoiceAsVerificationProof {

    public static final String ROLE_NAME_FOR_IBAN_PROOF = BankAccount_attachPdfAsVerificationProof.ROLE_NAME_FOR_IBAN_PROOF;
    public static final Predicate<Paperclip> HAS_IBAN_PROOF_DOCTYPE = paperclip -> DocumentTypeData.IBAN_PROOF.isDocTypeFor(paperclip.getDocument());

    private final BankAccount bankAccount;

    public BankAccount_attachInvoiceAsVerificationProof(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Action()
    @MemberOrder(name = "documents", sequence = "1")
    public BankAccount act(final Document document) {
        paperclipRepository.attach(document, ROLE_NAME_FOR_IBAN_PROOF, bankAccount);
        return bankAccount;
    }

    public List<Document> choices0Act() {
        final List<Document> documents = Lists.newArrayList();
        final List<IncomingInvoice> invoices = incomingInvoiceRepository.findByBankAccount(bankAccount);
        for (IncomingInvoice invoice : invoices) {
            final List<Document> attachedDocuments =
                    lookupAttachedPdfService.lookupIncomingInvoicePdfsFrom(invoice);
            documents.addAll(attachedDocuments);
        }

        final List<Document> documentsCurrentlyAttachedAsIbanProof = paperclipRepository
                .findByAttachedToAndRoleName(bankAccount, ROLE_NAME_FOR_IBAN_PROOF).stream()
                .map(Paperclip::getDocument)
                .filter(Document.class::isInstance)
                .map(Document.class::cast)
                .collect(Collectors.toList());

        documents.removeAll(documentsCurrentlyAttachedAsIbanProof);
        return documents;
    }

    public String disableAct() {

        final Optional<Paperclip> ibanProofDoctypePaperclipIfAny = paperclipRepository
                .findByAttachedToAndRoleName(bankAccount, ROLE_NAME_FOR_IBAN_PROOF).stream()
                .filter(HAS_IBAN_PROOF_DOCTYPE).findAny();
        if(ibanProofDoctypePaperclipIfAny.isPresent()) {
            return "IBAN proof PDF has already been attached";
        }

        if (choices0Act().isEmpty()) {
            return "No (other) invoices found for this bank account";
        }

        return null;
    }


    @Inject
    IncomingInvoiceRepository incomingInvoiceRepository;

    @Inject
    LookupAttachedPdfService lookupAttachedPdfService;

    @Inject
    PaperclipRepository paperclipRepository;

}
