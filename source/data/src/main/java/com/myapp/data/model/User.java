package com.myapp.data.model;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.SchemaOutputResolver;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class User
//        implements InitializingBean
//        , DisposableBean
{

    private Long id;
    private String name;
    private Integer sex;
    private String mobile;
    private Address address;
    private List<String> list;
    private Set<String> set;
    private Map<Long, String> map;
    private Properties properties;
    private Resource resource;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<Long, String> getMap() {
        return map;
    }

    public void setMap(Map<Long, String> map) {
        this.map = map;
    }

    public User() {
    }

    //    @ConstructorProperties({"id1", "mobile1", "name1"})
    public User(Long id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public User(String name, Integer sex) {
        this.name = name;
        this.sex = sex;
    }

    public User(String name) {
        this.name = name;
    }

    public static User staticCreateMethod(Long id, String name, String mobile) {
        return new User(id, name, mobile);
    }

    public User instanceCreateMethod(Long id, String name, String mobile) {
        return new User(id, name, mobile);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address=" + address +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", properties=" + properties +
                '}';
    }

//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("【postConstruct()】");
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        System.out.println("【preDestroy()】");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("【afterPropertiesSet()】");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("【destroy()】");
//    }
//
//    public void myInit() {
//        System.out.println("【myInit()】");
//    }
//
//    public void myDestroy() {
//        System.out.println("【myDestroy()】");
//    }

    public static class IdentityCard {

        private String number;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "IdentityCard{" +
                    "number='" + number + '\'' +
                    '}';
        }
    }
}
