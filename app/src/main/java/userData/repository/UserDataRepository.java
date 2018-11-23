package userData.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import activity.ContextSingleton;
import userData.UserData;

public class UserDataRepository implements IUserDataRepository {

	private static final String FILE_NAME = "UserData.json";

	@Override
	public void create(UserData model) {

		File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);
		try {
			if (model != null) {

				if (!file.exists()) {

					file.createNewFile();
					new ObjectMapper().writeValue(file, model);
				}
				else {
					file.delete();
				}
			}

		} catch (IOException e) {

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
		if (file.exists()) {

			try {
				model = new ObjectMapper().readValue(file, UserData.class);
			} catch (IOException e) {
				//Just ignore it
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
}
