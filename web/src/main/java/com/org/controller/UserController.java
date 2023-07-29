package com.org.controller;

import com.org.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.org.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * 获取用户列表
 * @author Leo
 * @date 18:48 2019/5/22
 **/
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * getUserList()
     * @author Leo
     * @date 18:49 2019/5/22
     * @param model
     * @return java.lang.String
     **/
    @RequestMapping("/list")
    public String toList(Model model){
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Hello World! This is a test PDF document."));
            document.close();
            System.out.println("PDF Created!");

            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            // Now 'is' contains the PDF data. You can use this InputStream to upload the PDF to your OSS.

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "list";
    }
}