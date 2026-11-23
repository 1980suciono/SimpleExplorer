package com.indolibs.libs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleExplorer {

    public SimpleExplorer() {}

    public List<HashMap<String, String>> getListDirectory(String strPath,boolean deep) {
        return (deep)?scanDirectory(strPath, new HashSet<>()):scanDirectory(strPath, null);
    }
	
	 public List<HashMap<String, String>> getListDirectory(String strPath) {
        return getListDirectory(strPath, false);
    }
	
    private List<HashMap<String, String>> scanDirectory(String strPath, Set<String> visited) {
        List<HashMap<String, String>> listItems = new ArrayList<>();
        File folder = new File(strPath);
        File[] items = folder.listFiles();

        if (items != null) {
            for (File item : items) {
                String absPath = item.getAbsolutePath();
                if (visited!=null&&!visited.add(absPath)) {
                    continue; // lewati jika path sudah pernah masuk
                }

                HashMap<String, String> entry = new HashMap<>();
                entry.put("name", item.getName());
                entry.put("path", absPath);

                if (item.isDirectory()) {
                    entry.put("type", "folder");
                    listItems.add(entry); // induk masuk dulu sebelum isi subfolder secara recursive asal (visited!=null)
                    if (visited!=null)listItems.addAll(scanDirectory(absPath, visited));
                } else if (item.isFile()) {
                    entry.put("type", "file");
                    listItems.add(entry);
                } else {
                    entry.put("type", "other");
                    listItems.add(entry);
                }
            }
        }
        return listItems;
    }
	
	public boolean delFile(String strPath) {
		if (strPath == null) {
			return false;
		}
        File file = new File(strPath);
        if (file.exists()) {
            return file.delete();
        }
		return false;
    }
	
	public boolean delEmptyFolder(String strPath) {
		if (strPath == null) {
			return false;
		}
		
		File folder = new File(strPath);
		if (!folder.exists() || !folder.isDirectory()) {
			return false;
		}

		File[] contents = folder.listFiles();
		if (contents == null) {
			return false;
		}
		
		if (contents.length == 0) {
			return folder.delete();
		} 
		return false;
	}
	
	public boolean delFiles(String strPath) {
		return delFiles(strPath, true);
	}

	public boolean delFilesIgnoringSubfolders(String strPath) {
		return delFiles(strPath, false);
	}
	
	private boolean delFiles(String strPath,boolean strictNoSubfolder) {
		if (strPath == null) {
			return false;
		}
		
		File folder = new File(strPath);
		if (!folder.exists() || !folder.isDirectory()) {
			return false;
		}

		File[] items = folder.listFiles();
		
		if (items == null) {
			return false;
		}
		
		if(strictNoSubfolder){
			for (File item : items) {
				if (item.isDirectory()) {
					return false;
				}
			}
		}
		
		boolean allFilesDeleted = true;

		for (File item : items) {
			if (item.isFile()) {
				if (!item.delete()) {
					allFilesDeleted = false;
				}
			}
		}

		return allFilesDeleted;
	}
	
	public boolean copyFile(String source, String target) {
		if (source == null || target == null) {
			return false;
		}

		File srcFile = new File(source);
		File dstFile = new File(target);

		// Pastikan source ada dan bukan folder
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}

		// Jika target ternyata mengarah ke folder, arahkan ke nama file yang sama di dalam folder itu
		if (dstFile.isDirectory()) {
			dstFile = new File(dstFile, srcFile.getName());
		}

		// Buat folder induk jika belum ada
		File parentDir = dstFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		byte[] buffer = new byte[8192]; // buffer 8 KB agar proses salin lebih cepat
		try (FileInputStream fis = new FileInputStream(srcFile);
			 FileOutputStream fos = new FileOutputStream(dstFile)) {

			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			return true;
		} catch (IOException e) {
			System.err.println("Gagal menyalin file: " + e.getMessage());
			return false;
		}
	}
}