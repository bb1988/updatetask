package com.bb.updatetask;

/**
 * function: 登录返回实体类
 * author: zhengzq
 * date: 2019/5/20  15:10
 */
public class UserInfoEntity {

    /**
     * avatar : /userfiles/zzq.jpg
     * code : 6ab5ec61-ab4a-4050-9f96-07bd3df02b88
     * companyCode : XFQ
     * companyName : 忻府区
     * duty : 3
     * dutyName : 班组长
     * email :
     * empCode : 45
     * empName : 变电33
     * gps :
     * hatSip :
     * idNumber :
     * officeCode : XFQBDS2
     * officeName : 忻府区变电所2
     * password :
     * phone :
     * phoneSip :
     * registered : 0
     * sex : 1
     * sexName : 男
     * userId : 45
     * username : user4
     */

    private String avatar;
    private String code;
    private String companyCode;
    private String companyName;
    private int duty;
    private String dutyName;
    private String email;
    private int empCode;
    private String empName;
    private String gps;
    private String hatSip;
    private String idNumber;
    private String officeCode;
    private String officeName;
    private String password;
    private String phone;
    private String phoneSip;
    private int registered;
    private int sex;
    private String sexName;
    private int userId;
    private String username;
    private InfoMap infoMap;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getHatSip() {
        return hatSip;
    }

    public void setHatSip(String hatSip) {
        this.hatSip = hatSip;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneSip() {
        return phoneSip;
    }

    public void setPhoneSip(String phoneSip) {
        this.phoneSip = phoneSip;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InfoMap getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(InfoMap infoMap) {
        this.infoMap = infoMap;
    }

    public class InfoMap {
        public String province;
        public String officeLevel;
        public String city;
        public String county;
        public String officeAddress;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getOfficeLevel() {
            return officeLevel;
        }

        public void setOfficeLevel(String officeLevel) {
            this.officeLevel = officeLevel;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getOfficeAddress() {
            return officeAddress;
        }

        public void setOfficeAddress(String officeAddress) {
            this.officeAddress = officeAddress;
        }
    }
}
