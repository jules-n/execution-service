package com.ynero.ss.execution.persistence.user;

import com.ynero.ss.execution.domain.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    @Setter(onMethod_ = {@Autowired})
    private MongoTemplate mongoTemplate;
    @Setter(onMethod_ = {@Autowired})
    PasswordEncoder encoder;

    @Override
    public boolean addRights(String username, Set<String> rights) {
        var update = new Update();
        rights.forEach(
                right -> update.addToSet("roles", right)
        );
        Criteria criteria = new Criteria("username").is(username);
        var result = mongoTemplate.updateFirst(new Query(criteria), update, User.COLLECTION_NAME);
        return result.wasAcknowledged();
    }

    @Override
    public boolean update(User user) {
        Update update = new Update();
        update.set("password", encoder.encode(user.getPassword()));
        Criteria criteria = new Criteria("username").is(user.getUsername());
        var result = mongoTemplate.updateFirst(new Query(criteria), update, User.COLLECTION_NAME);
        return result.wasAcknowledged();
    }

    @Override
    public boolean delete(String username) {
        Criteria criteria = new Criteria("username").is(username);
        var result = mongoTemplate.remove(new Query(criteria), User.COLLECTION_NAME);
        return result.getDeletedCount()==1;
    }
}
