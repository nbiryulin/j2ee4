package model;

import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;
import exceptions.NoSuchModelNameException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Moto implements Transport, Serializable {

    private static final long serialVersionUID = 2L;

    public Moto() {
    }

    public Moto(String mark, int size) {
        this.mark = mark;
        try {
            for (int i = 1; i < size + 1; i++) {
                addModel("model.Moto " + i, i);
            }
        } catch (DuplicateModelNameException e) {
            e.printStackTrace();
        }

    }

    private String mark;

    @Override
    public String getMark() {
        return mark;
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public void setModelName(String oldName, String newName) throws DuplicateModelNameException, NoSuchModelNameException {
        Map<String, Model> map = modelsToMap();
        if (map.containsKey(newName)) {
            throw new DuplicateModelNameException();
        } else if (!map.containsKey(oldName)){
            throw new NoSuchModelNameException();
        }
        map.get(oldName).setName(newName);
    }


    @Override
    public String[] getModelNames() {
        String[] array = new String[size];
        Model model = head;
        for (int i = 0; i < size; i++) {
            model = model.next;
            array[i] = model.name;
        }
        return array;
    }

    @Override
    public double getPriceByName(String name) throws NoSuchModelNameException {
        Map<String, Model> map = modelsToMap();
        if (!map.containsKey(name)) {
            throw new NoSuchModelNameException();
        }
        return map.get(name).price;
    }

    @Override
    public void setPriceByName(String name, double price) throws NoSuchModelNameException {
        Map<String, Model> map = modelsToMap();
        if (!map.containsKey(name)) {
            throw new NoSuchModelNameException();
        } else if (Double.isNaN(price)) {
            throw new ModelPriceOutOfBoundsException();
        }
        map.get(name).price = price;
    }

    public double[] getPrices() {
        Map<String, Model> map = modelsToMap();
        return map.values().stream().mapToDouble(Model::getPrice).toArray();
    }

    @Override
    public void addModel(String name, double price) throws DuplicateModelNameException {
        if (modelsToMap().containsKey(name)) {
            throw new DuplicateModelNameException();
        } else if (price < 0) {
            throw new ModelPriceOutOfBoundsException();
        }
        Model last = head;
        for (int i = 0; i < size; i++) {
            last = last.next;
        }
        Model model = new Model(name, price, head, last);
        last.next = model;
        head.prev = model;
        size++;
    }

    @Override
    public void deleteModel(String name, double price) throws NoSuchModelNameException {
        if (!modelsToMap().containsKey(name)) {
            throw new NoSuchModelNameException();
        }
        Model model = head;
        for (int i = 0; i < size; i++) {
            if (model.next.name.equals(name)) {
                Model after = model.next.next;
                after.prev = model;
                model.next = after;
                size--;
                return;
            } else {
                model = model.next;
            }
        }
    }

    @Override
    public int getModelsLength() {
        return size;
    }


    private Map<String, Model> modelsToMap() {
        Map<String, Model> map = new HashMap<>();
        Model model = head;
        for (int i = 0; i < size; i++) {
            model = model.next;
            map.put(model.name, model);
        }
        return map;
    }

    public Map<String, Double> getModelsMap() {
        Map<String, Double> map = new HashMap<>();
        modelsToMap().forEach((key, value) -> map.put(key, value.getPrice()));
        return map;
    }

    static class Model implements Serializable {
        String name = null;
        double price = Double.NaN;
        Model prev = null;
        Model next = null;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Double.compare(model.price, price) == 0 && Objects.equals(name, model.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, price);
        }

        public Model(String name, double price, Model prev, Model next) {
            this.name = name;
            this.price = price;
            this.prev = prev;
            this.next = next;
        }

        public Model() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }
    }

    private Model head = new Model();

    {
        head.prev = head;
        head.next = head;
    }

    private int size = 0;
}
