package com.tytzy.base.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 *  文件名：FileUtils
 *  版权：Copyright © 2016 RMD Inc. All Rights Reserved
 *  描述：对sd卡的文件相关操作
 *  创建人：白勃
 *  创建时间：2016/9/6 12:53
 *  版本号：0.1
 */
public class FileUtils {



	/**
	 * 判断sdcrad是否已经安装
	 * @return boolean true安装 false 未安装
	 */
	public static boolean isSDCardMounted(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	/**
	 * 得到sdcard的路径
	 * @return
	 */
	public static String getSDCardRoot(){
		System.out.println(isSDCardMounted()+ Environment.getExternalStorageState());
		if(isSDCardMounted()){
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return "";
	}

	//建立文件夹
	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			Log.i("error:", e+"");
		}
	}
	/**
	 * 创建文件的路径及文件
	 * @param path 路径，方法中以默认包含了sdcard的路径，path格式是"/path...."
	 * @param filename 文件的名称
	 * @return 返回文件的路径，创建失败的话返回为空
	 */
	public static String createMkdirsAndFiles(String path, String filename) {
		if (TextUtils.isEmpty(path)) {
			throw new RuntimeException("路径为空");
		}
		path = getSDCardRoot()+path;
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
				throw new RuntimeException("创建文件夹不成功");
			}
		} 
		File f = new File(file, filename);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("创建文件不成功");
			}
		}
		return f.getAbsolutePath();
	}
	
	/**
	 * 把内容写入文件
	 * @param path 文件路径
	 * @param text 内容
	 */
	public static void write2File(String path, String text, boolean append){
		BufferedWriter bw = null;
		try {
			//1.创建流对象
			bw = new BufferedWriter(new FileWriter(path,append));
			//2.写入文件
			bw.write(text);
			//换行刷新
			bw.newLine();
			bw.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//4.关闭流资源
			if(bw!= null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 删除文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path){
		if(TextUtils.isEmpty(path)){
			throw new RuntimeException("路径为空");
		}
		File file = new File(path);
		if(file.exists()){
			try {
				file.delete();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取文件夹大小
	 * @param file File实例
	 * @return long
	 */
	public static long getFolderSize(File file){

		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++)
			{
				if (fileList[i].isDirectory())
				{
					size = size + getFolderSize(fileList[i]);

				}else{
					size = size + fileList[i].length();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return size/1048576;
		return size;
	}

	/**
	 * 转换文件大小
	 *
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	//递归删除文件及文件夹
	public static void deleteFiles(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFiles(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * 删除指定目录下文件及目录
	 * @param deleteThisPath
//	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {// 处理目录
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
						LogUtils.e("删除文件成功","");
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
							LogUtils.e("删除目录成功","");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
	* 拷贝文件到新文件
	* 复制整个文件夹内容
    * @param oldPath String 原文件路径 如：c:/fqf
    * @param newPath String 复制后路径 如：f:/fqf/ff
    * @return boolean
	* */
	public static int copyFolder(String fromFile, String toFile) {

		//要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		//如同判断SD卡是否存在或者文件是否存在
		//如果不存在则 return出去
		if(!root.exists())
		{
			return -1;
		}
		//如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		//目标目录
		File targetDir = new File(toFile);
		//创建目录
		if(!targetDir.exists())
		{
			targetDir.mkdirs();
		}
		//遍历要复制该目录下的全部文件
		for(int i= 0;i<currentFiles.length;i++)
		{
			if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
			{
				copyFolder(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

			}else//如果当前项为文件则进行文件拷贝
			{
				CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
				LogUtils.e("复制文件成功","");
			}
		}
		return 0;
	}
	//文件拷贝
	//要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int CopySdcardFile(String fromFile, String toFile)
	{

		try
		{
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex)
		{
			LogUtils.e("exception",ex.getMessage());
			return -1;
		}
	}

	/**
	 * 写入文件
	 *
	 * @param in
	 * @param file
	 */
	public static void writeFile(InputStream in, File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

//		if (file != null && file.exists())
//			file.delete();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024 * 128];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();

	}


	/**
	 * 功能描述：复制单个文件
	 *
	 * @param srcFileName  待复制的文件名
	 * @param descFileName 目标文件名
	 * @param coverlay     如果目标文件已存在，是否覆盖
	 * @return 返回： 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName,
                                        String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
//			System.out.println("复制文件失败，源文件" + srcFileName + "不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
//			System.out.println("复制文件失败，" + srcFileName + "不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
//				System.out.println("目标文件已存在，准备删除!");
				if (!deleteFile(descFileName)) {
//					System.out.println("删除目标文件" + descFileName + "失败!");
					return false;
				}
			} else {
//				System.out.println("复制文件失败，目标文件" + descFileName + "已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
//				System.out.println("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
//					System.out.println("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
//			System.out.println("复制单个文件" + srcFileName + "到" + descFileName
//					+ "成功!");
			return true;
		} catch (Exception e) {
			System.out.println("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}


	/**
	 * 根据Uri获取图片的绝对路径
	 *
	 * @param context 上下文对象
	 * @param uri     图片的Uri
	 * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
	 */
	public static String getRealPathFromUri(Context context, Uri uri) {
		int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion >= 19) { // api >= 19
			return getRealPathFromUriAboveApi19(context, uri);
		} else { // api < 19
			return getRealPathFromUriBelowAPI19(context, uri);
		}
	}

	/**
	 * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
	 *
	 * @param context 上下文对象
	 * @param uri     图片的Uri
	 * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
	 */
	private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
		return getDataColumn(context, uri, null, null);
	}

	/**
	 * 适配api19及以上,根据uri获取图片的绝对路径
	 *
	 * @param context 上下文对象
	 * @param uri     图片的Uri
	 * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
	 */
	@SuppressLint("NewApi")
	private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
		String filePath = null;
		if (DocumentsContract.isDocumentUri(context, uri)) {
			// 如果是document类型的 uri, 则通过document id来进行处理
			String documentId = DocumentsContract.getDocumentId(uri);
			if (isMediaDocument(uri)) { // MediaProvider
				// 使用':'分割
				String id = documentId.split(":")[1];

				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = {id};
				filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
			} else if (isDownloadsDocument(uri)) { // DownloadsProvider
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
				filePath = getDataColumn(context, contentUri, null, null);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// 如果是 content 类型的 Uri
			filePath = getDataColumn(context, uri, null, null);
		} else if ("file".equals(uri.getScheme())) {
			// 如果是 file 类型的 Uri,直接获取图片对应的路径
			filePath = uri.getPath();
		}
		return filePath;
	}

	/**
	 * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
	 *
	 * @return
	 */
	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		String path = null;

		String[] projection = new String[]{MediaStore.Images.Media.DATA};
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
				path = cursor.getString(columnIndex);
			}
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * @param uri the Uri to check
	 * @return Whether the Uri authority is MediaProvider
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri the Uri to check
	 * @return Whether the Uri authority is DownloadsProvider
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

}
