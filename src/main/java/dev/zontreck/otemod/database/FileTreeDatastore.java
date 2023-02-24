package dev.zontreck.otemod.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.loading.FMLPaths;

public class FileTreeDatastore {
    private static final Path BASE;

    static{
        Path X = FMLPaths.CONFIGDIR.get().resolve("otemod");
        BASE=X;
        if(!BASE.toFile().exists())
        {
            try {
                Files.createDirectory(BASE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Path of(String nick)
    {
        return BASE.resolve(nick);
    }

    public static List<File> getListOfFiles(Path files)
    {
        List<File> fileList = new ArrayList<>();

        File[] entries = files.toFile().listFiles();
        for (File file : entries) {
            fileList.add(file);
        }
        return fileList;
    }
}
