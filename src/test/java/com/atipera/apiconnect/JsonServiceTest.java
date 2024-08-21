package com.atipera.apiconnect;

import static org.junit.jupiter.api.Assertions.*;

import com.atipera.apiconnect.model.Owner;
import com.atipera.apiconnect.model.RepositoryInfo;
import com.atipera.apiconnect.service.JsonService;
import org.junit.jupiter.api.Test;

import java.util.List;

class JsonServiceTest {

    private JsonService jsonService = new JsonService();

    @Test
    void saveToJsonAndConvertToXml_validData_savesXmlFile() {
        List<RepositoryInfo> mockRepositories = List.of(
                new RepositoryInfo("repo1", new Owner("shoshinmas"), false, "https://api.github.com/repos/shoshinmas/repo1/branches{/branch}")
        );

        String xmlPath = jsonService.saveToJsonAndConvertToXml(mockRepositories);

        assertEquals("/path/to/xml/file.xml", xmlPath);
        // Additional assertions to check if the XML file is created properly
    }

    @Test
    void getXml_existingUser_returnsXmlContent() {
        String username = "shoshinmas";
        String xmlContent = jsonService.getXml(username);

        assertEquals("<xml></xml>", xmlContent);
    }
}

