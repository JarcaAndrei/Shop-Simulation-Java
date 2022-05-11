package com.company.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AuditService {
    // singleton
    private static AuditService makeAuditInstance = null;
    private String path = "./date/Audit.csv";
    private FileWriterService gout = FileWriterService.getInstance();

    private AuditService() { }

    public static AuditService getInstance() {
        if (makeAuditInstance == null)
            makeAuditInstance = new AuditService();
        return makeAuditInstance;
    }

    public void log(String numeActiune) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ArrayList<String> out_message =new ArrayList<String>();
        out_message.add(numeActiune);
        out_message.add(timestamp);
        gout.write(path,out_message);
    }

}