package com.e.rssapplication.DataBase;

//Các enum định nghĩa các loại tin
public enum EnumTypeNews {
    HOMEPAGE("Trang chủ"),
    NEWS("Thời sự"),
    WORLD("Thế giới"),
    ECONOMY("Kinh tế"),
    LAW("Pháp luật"),
    CULTURAL("Văn hóa"),
    EDUCATION("Giáo dục"),
    SPORT("Thể thao"),
    SCIENCEANDTECHNOLOGY("Khoa học - Công nghệ"),
    HEALTH("Sức khỏe");

    private final String name;

    private EnumTypeNews(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
