package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class UserFixture {

  private static User createUser(String name, String email) {
    User user = new User(name, email, "1234", null);
    return user;
  }

  public static List<User> SetMockUsers(UserRepository userRepository) {
    userRepository.deleteAll();

    List<User> users = new ArrayList<>();
    users.add(UserFixture.createUser("test1", "test1@test.com"));
    users.add(UserFixture.createUser("test2", "test2@test.com"));
    users.add(UserFixture.createUser("test3", "test3@test.com"));
    users.add(UserFixture.createUser("test4", "test4@test.com"));

    userRepository.save(users.get(0));
    userRepository.save(users.get(1));
    userRepository.save(users.get(2));
    userRepository.save(users.get(3));

    return users;
  }
}
