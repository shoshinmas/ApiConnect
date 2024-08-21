package com.atipera.apiconnect.service;

import com.atipera.apiconnect.model.RepositoryInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonService {

    public String saveToJsonAndConvertToXml(List<RepositoryInfo> repositories) {
        // Convert repositories to JSON and save to file
        // Convert JSON to XML and save to file
        // Return the path to the saved XML file
        return "/path/to/xml/file.xml";
    }

    public String getXml(String username) {
        // Load the XML content from the file based on the username
        return "<xml></xml>";
    }
}

