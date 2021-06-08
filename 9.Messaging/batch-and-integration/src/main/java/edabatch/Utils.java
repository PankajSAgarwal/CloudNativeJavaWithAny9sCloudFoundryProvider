package edabatch;

import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

abstract class Utils {

    public static void mv(File in, File out) {
        try{
            Assert.isTrue(out.exists() || out.mkdirs());
            File target = new File(out, in.getName());
            Files.copy(in.toPath(),target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
