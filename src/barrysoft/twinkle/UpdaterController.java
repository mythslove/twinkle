package barrysoft.twinkle;

import java.io.File;

import org.apache.log4j.Logger;

import barrysoft.twinkle.view.UpdaterView;
import barrysoft.twinkle.view.UpdaterViewObserver;
import barrysoft.utils.FileUtils;

/**
 * <p>This is the controller class entitled of handling the
 * updated process through the {@link Updater} class.</p>
 * 
 * <p>It's just an abstraction of the {@code Updater} class that
 * can be used in a simple MVC pattern. It acts as a simple proxy
 * between a {@link UpdaterView} and an {@code Updater} by implementing
 * both {@link UpdaterObserver} and {@link UpdaterViewObserver}</p>
 * 
 * @author Daniele Rapagnani
 */

public class UpdaterController implements UpdaterObserver, UpdaterViewObserver 
{
	private final Updater updater;
	private final UpdaterView view;
	
	/**
	 * Creates a new UpdaterController using the provided
	 * updater instance to execute update tasks and linked
	 * to the provided {@link UpdaterView}
	 * 
	 * @param updater	An {@link Updater} instance
	 * @param view		A {@link UpdaterView} instance
	 */
	
	public UpdaterController(Updater updater, UpdaterView view)
	{
		this.updater = updater;
		this.view = view;
		
		this.updater.addObserver(this);
		this.view.addObserver(this);
	}
	
	/**
	 * Given an array of {@link UpdateRequest} check if
	 * there are new updates available.
	 * 
	 * @param requests {@link UpdateRequest} instances
	 * 					holding the required informations
	 * 					for checking for available updates
	 */
	
	public void checkUpdates(UpdateRequest ...requests)
	{		
		try {
			updater.checkUpdates(requests);
		} catch (UpdateException e) {
			view.updateError(e);
		}
	}

	@Override
	public void newVersionFound(UpdateVersion version, UpdateRequest source)
	{		
		String displayVersion = version.getShortVersion();
		
		if (displayVersion == null)
			displayVersion = version.getVersion();
		
		Logger.getLogger(getClass()).debug(String.
				format("New version found *%s* - %s %s",
				displayVersion, 
				version.getDownloadUrl().toString(), 
				FileUtils.bytesToSize(version.getDownloadSize())));
		
		getView().newVersionFound(version, source);
	}
	
	@Override
	public void noUpdateRequired()
	{
		Logger.getLogger(getClass()).debug("Application is up to date");
		getView().noUpdateRequired();
	}

	@Override
	public void downloadCompleted(UpdateVersion version)
	{
		Logger.getLogger(getClass()).debug("Update files download completed.");
		getView().downloadCompleted(version);
	}

	@Override
	public void downloadProgress(UpdateVersion version, int bytesLoaded)
	{
		getView().downloadProgress(version, bytesLoaded);
	}

	@Override
	public void downloadStarted(UpdateVersion version)
	{
		Logger.getLogger(getClass()).debug("Downloading update files...");
		getView().downloadStarted(version);
	}

	@Override
	public void updateRequested(final UpdateVersion version, final UpdateRequest source)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try 
				{
					updater.update(version, source);
				} 
				catch (UpdateException e) 
				{
					view.updateError(e);
					
					if (Logger.getLogger(getClass()).isDebugEnabled())
						e.printStackTrace();
				}
			}

		}).start();
	}
	
	@Override
	public void updateCanceled()
	{
		view.updateCanceled();
	}
	
	protected UpdaterView getView()
	{
		return view;
	}

	@Override
	public void userCanceledUpdate()
	{
		updater.cancelUpdate();
	}

	@Override
	public void extractionEnded(File archiveFile)
	{
		view.extractionEnded(archiveFile);
	}

	@Override
	public void extractionStarted(File archiveFile)
	{
		view.extractionStarted(archiveFile);
	}

	@Override
	public void validationEnded(UpdateVersion version, File archiveFile)
	{
		view.validationEnded(version, archiveFile);
	}

	@Override
	public void validationStarted(UpdateVersion version, File archiveFile)
	{
		view.validationStarted(version, archiveFile);
	}

	@Override
	public void updateCompleted()
	{
		view.updateCompleted();
	}

	@Override
	public void checkingEnded(UpdateRequest source)
	{
		view.checkingStarted(source);
	}

	@Override
	public void checkingStarted(UpdateRequest source)
	{
		view.checkingEnded(source);
	}

	@Override
	public void restartRequired(UpdateRequest source)
	{
		view.restartRequired(source);
	}

	@Override
	public void restartRequested(UpdateRequest source)
	{
		updater.restart(source);
	}
}
