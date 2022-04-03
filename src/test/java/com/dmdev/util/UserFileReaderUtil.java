package com.dmdev.util;

import com.dmdev.dto.UserTestDto;
import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserFileReaderUtil {
    @SneakyThrows
    public List<UserTestDto> readAllTestUsersFromCsv(String csvFilePath) {
        return readAllCsv(csvFilePath).stream()
                .skip(1)
                .map(userText -> {
                    String id = userText[0];
                    String name = userText[1];
                    String birhday = userText[2];
                    String email = userText[3];
                    String password = userText[4];
                    String role = userText[5];
                    String gender = userText[6];
                    return UserTestDto.of(id, name, birhday, email, password, role, gender);
                })
                .collect(Collectors.toList());
    }

    private List<String[]> readAllCsv(String csvFilePath) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(
                UserFileReaderUtil.class.getClassLoader().getResource(csvFilePath).toURI()));
        return new CSVReader(reader).readAll();
    }

}
