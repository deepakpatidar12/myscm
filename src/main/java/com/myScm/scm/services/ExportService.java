package com.myScm.scm.services;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.myScm.scm.Dto.ContactForm;

public interface ExportService {


    ByteArrayInputStream generatePdf(List<ContactForm> contactForm);

    ByteArrayInputStream generateExcel(List<ContactForm> contactForms);

}
