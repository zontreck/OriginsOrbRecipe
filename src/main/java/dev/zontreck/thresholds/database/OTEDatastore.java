package dev.zontreck.thresholds.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import dev.zontreck.libzontreck.util.FileTreeDatastore;

public class OTEDatastore extends FileTreeDatastore
{
    private static final Path OTEBASE;

    static{
        Path X = FileTreeDatastore.of("otemod");
        OTEBASE=X;
        if(!OTEBASE.toFile().exists())
        {
            try {
                Files.createDirectory(OTEBASE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Path of(String nick)
    {
        return OTEBASE.resolve(nick);
    }
}
