package dev.yafatek.networking.v1.models;

import java.util.List;
import java.util.Objects;

public class Body<T> {
    private List<T> list;

    public Body(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body<?> body = (Body<?>) o;
        return Objects.equals(list, body.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "Body{" +
                "list=" + list +
                '}';
    }
}
