package com.example.backend.services.impl;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.sql.Connection;

@Service
public class ReportService {

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public JasperPrint generateJasperPrint(InputStream inputStream, Map<String, Object> parameters)
            throws JRException, SQLException {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
        return JasperFillManager.fillReport(jasperReport, parameters, getConnection());
    }

}