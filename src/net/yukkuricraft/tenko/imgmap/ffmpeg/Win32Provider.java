package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

public class Win32Provider extends Provider {

	private WeakReference<ImgMap> plugin;

	public Win32Provider(ImgMap plugin){
		super(new File(plugin.getDataFolder(), "ffmpeg.exe"));
		this.plugin = new WeakReference<ImgMap>(plugin); //Keep a weak reference.
	}

	@Override
	public boolean execute(String video_id){
		try {
			if(plugin.get() != null){
				ProcessBuilder procbuild = new ProcessBuilder();
				procbuild.command(win32Args(video_id, new File(plugin.get().getLVideosDirectory(), video_id + ".gif")));
				procbuild.redirectErrorStream(true);

				Process process = procbuild.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String l;
				while((l = reader.readLine()) != null){
					logger.info(l);
				}
				reader.close();
				return true;
			} else {
				throw new IllegalStateException("The primary plugin object was garbage collected! Is ImgMap loaded?");
			}
		} catch (Throwable t){
			t.printStackTrace();
			return false;
		}
	}

}