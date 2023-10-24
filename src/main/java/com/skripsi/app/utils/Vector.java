package com.skripsi.app.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector {
  private List<Double> vect;

  public Vector(int size) {
    this.vect = new ArrayList<Double>(size);
    for (int i = 0; i < size; i++) {
      this.vect.add(i, 0.0);
    }
  }

  public Vector(int size, Double initValue) {
    this.vect = new ArrayList<Double>(size);
    for (int i = 0; i < size; i++) {
      this.vect.add(i, initValue);
    }
  }

  public Vector(List<Double> vect) {
    this.setVect(vect);
  }

  public Vector(Double[] vect) {
    this.setVect(Arrays.asList(vect));
  }
  
  public void setVect(Double[] vect) {
    this.setVect(Arrays.asList(vect));
  }

  public void setVect(List<Double> vect) {
    this.vect = vect;
  }

  public List<Double> getVect() {
    return this.vect;
  }

  public void setElm(int index, Double value) {
    this.vect.set(index, value);
  }

  public Double getElm(int index) {
    return this.vect.get(index);
  }

  public void append(Double value) {
    if(this.vect == null){
        this.vect = new ArrayList<Double>();
    }
    this.vect.add(value);
  }

  public int getSize() {
    if (vect == null)
      return -1;
    return vect.size();
  }

  public static Vector add(Vector v1, Vector v2) {
    if (v1 == null || v2 == null || v1.getSize() == 0 || v2.getSize() == 0)
      return null;
    if (v1.getSize() != v2.getSize())
      return null;

    Vector res = new Vector(v1.getSize());
    for (int i = 0; i < v1.getSize(); i++) {
      res.setElm(i, v1.getElm(i) + v2.getElm(i));
    }
    return res;
  }

  public static Vector subt(Vector v1, Vector v2) {
    if (v1 == null || v2 == null || v1.getSize() == 0 || v2.getSize() == 0)
      return null;
    if (v1.getSize() != v2.getSize())
      return null;

    Vector res = new Vector(v1.getSize());
    for (int i = 0; i < v1.getSize(); i++) {
      res.setElm(i, v1.getElm(i) - v2.getElm(i));
    }
    return res;
  }

  public static Vector elmMul(Vector v1, Vector v2) {
    if (v1 == null || v2 == null || v1.getSize() == 0 || v2.getSize() == 0)
      return null;
    if (v1.getSize() != v2.getSize())
      return null;

    Vector res = new Vector(v1.getSize());
    for (int i = 0; i < v1.getSize(); i++) {
      res.setElm(i, v1.getElm(i) * v2.getElm(i));
    }
    return res;
  }

  public static Vector mul(Vector v, Double scalar) {
    if (v != null && v.getSize() == 0)
      return null;

    Vector res = new Vector(v.getSize());
    for (int i = 0; i < v.getSize(); i++) {
      res.setElm(i, v.getElm(i) * scalar);
    }
    return res;
  }

  public static Vector abs(Vector v) {
    for (int i = 0; i < v.getSize(); i++) {
      v.setElm(i, Math.abs(v.getElm(i)));
    }
    return v;
  }

  public static Double magnitude(Vector v) {
    Double sum = 0.0;
    for (int i = 0; i < v.getSize(); i++) {
      sum += v.getElm(i) * v.getElm(i);
    }
    return Math.sqrt(sum);
  }

  public static Vector constrain(Vector v, double min, double max) {
    Vector res = new Vector(v.getSize());
    for (int i = 0; i < res.getSize(); i++) {
      res.setElm(i, Math.max(min, Math.min(max, v.getElm(i))));
    }
    return res;
  }
}
