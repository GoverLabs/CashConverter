package userData.repository;

import userData.UserData;

public interface IUserDataRepository {

	void create(UserData model);
	void update(UserData model);
	UserData load();
	void clear();
}
