package userData.repository;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import activity.ContextSingleton;
import userData.UserData;

public class UserDataRepository implements IUserDataRepository {

	private static final String FILE_NAME = "UserData.ser";

	@Override
	public void create(UserData model) {

		File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			if (!file.exists())
				file.createNewFile();

			fout = new FileOutputStream(file);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(model);

		} catch (IOException e) {

		} finally {
			close(oos);
			close(fout);
		}
	}

	@Override
	public void update(UserData model) {
		clear();
		create(model);
	}

	@Override
	public UserData load() {

		UserData model = null;

		File file = new File(FILE_NAME);

		FileInputStream fin = null;
		ObjectInputStream ois = null;

		if (file.exists()) {
			try {

				fin = new FileInputStream(file);
				ois = new ObjectInputStream(fin);

				model = (UserData) ois.readObject();
			} catch (IOException e) {
				//Just ignore it
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				close(fin);
				close(ois);
			}
		}

		return model;
	}

	@Override
	public void clear() {
		File file = new File(FILE_NAME);

		if (file.exists()) {
			file.delete();
		}
	}

	private static void close(Closeable c) {
		if (c == null)
			return;

		try {
			c.close();
		} catch (IOException e) {
			// Ignore
		}
	}
}
