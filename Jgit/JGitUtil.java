package com.sunsheen.cns.edt.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class JGitUtil {

	/**
	 * 更新本地仓库，并把本地仓库同步到远程仓库
	 * 
	 * @param remotePath
	 *            远程地址
	 * @param branch
	 *            远程分支
	 * @param localPath
	 *            本地路径
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param msg
	 *            提交的备注
	 * @param folder
	 *            提交的文件夹 【.】：点是 全部仓库同步
	 * 
	 * @throws IOException
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 */
	public static void updateRemote(String remotePath, String branch, String localPath, String userName, String password, String msg, String folder) throws IOException, NoFilepatternException, GitAPIException {
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
		Git git = null;
		try {
			git = Git.open(new File(localPath));
			git.pull().call();
			git.add().setUpdate(false).addFilepattern(folder).call();// 添加
			git.add().setUpdate(true).addFilepattern(folder).call();// 添加 新增
			git.commit().setMessage(msg).call();
			git.push().setCredentialsProvider(usernamePasswordCredentialsProvider).setRemote("origin").setRefSpecs(new RefSpec(branch)).call();
			System.out.println("同步到远程Git仓库成功");
		} catch (RepositoryNotFoundException e) {
			System.out.println("创建本地Git仓库");
			git = Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
		}finally{
			git.close();
		}

	}

	/**
	 * 
	 * 从远程服务器更新到本地仓库
	 * 
	 * @param remotePath
	 *            远程地址
	 * @param branch
	 *            远程分支
	 * @param localPath
	 *            本地路径
	 * 
	 * @throws IOException
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 */
	public static void updateLocal(String remotePath, String branch, String localPath) throws IOException, NoFilepatternException, GitAPIException {
		Git git = null;
		try {
			git = Git.open(new File(localPath));
			git.pull().call();
			System.out.println("更新本地Git车库成功");
		} catch (RepositoryNotFoundException e) {
			System.out.println("创建本地Git仓库");
			git = Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
		}finally{
			git.close();
		}
	}
}
