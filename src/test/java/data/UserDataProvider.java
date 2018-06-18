package data;

import org.testng.annotations.DataProvider;

public class UserDataProvider {
    @DataProvider(name = "Credentials")
    public Object[][] userCredentials() {
        return new Object[][]{{"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"}};
    }
}
