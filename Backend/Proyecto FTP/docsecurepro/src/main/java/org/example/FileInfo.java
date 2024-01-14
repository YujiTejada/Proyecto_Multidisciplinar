package org.example;
public class FileInfo {

    private String name;
    private boolean isDirectory;
    private String url;

    public FileInfo(String name, boolean isDirectory, String url) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

}
