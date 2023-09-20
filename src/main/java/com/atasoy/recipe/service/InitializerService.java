package com.atasoy.recipe.service;

import java.io.IOException;
import java.sql.SQLException;

public interface InitializerService {
    void initializeSampleData() throws IOException, SQLException;
}
