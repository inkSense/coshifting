package org.coshift.a_domain.person;

public class Person {
    private final Long id; // community member number
    private String nickname;
    private String password;
    private long timeAccountId;
    private PersonRole role;

    public Person(Long id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.role = PersonRole.USER;
    }

    public Person(Long id, String nickname, String password, PersonRole role) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public Person(Long id, String nickname, String password, long timeAccountId, PersonRole role) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.timeAccountId = timeAccountId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public long getTimeAccountId() {
        return timeAccountId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setTimeAccountId(long timeAccountId) {
        this.timeAccountId = timeAccountId;
    }

    public PersonRole getRole() {
        return role;
    }

}
