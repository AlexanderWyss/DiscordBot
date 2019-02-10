package wyss.website.discordbot.music;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObservableList<E> extends ArrayList<E> {

  private static final long serialVersionUID = 2780525358637621254L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ObservableList.class);

  @Override
  public void add(int index, E element) {
    super.add(index, element);
    LOGGER.info("add Index: {} Element: {}", index, element);
    updateAll();
  }

  @Override
  public void clear() {
    super.clear();
    updateAll();
  }

  @Override
  public E set(int index, E element) {
    E set = super.set(index, element);
    updateAll();
    return set;
  }

  @Override
  public E remove(int index) {
    E remove = super.remove(index);
    updateAll();
    return remove;
  }

  @Override
  public boolean remove(Object o) {
    boolean remove = super.remove(o);
    updateAll();
    return remove;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean removeAll = super.removeAll(c);
    updateAll();
    return removeAll;
  }

  @Override
  public boolean removeIf(Predicate<? super E> filter) {
    boolean removeIf = super.removeIf(filter);
    updateAll();
    return removeIf;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean addAll = super.addAll(c);
    updateAll();
    return addAll;
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    boolean addAll = super.addAll(index, c);
    updateAll();
    return addAll;
  }

  @Override
  public boolean add(E e) {
    boolean add = super.add(e);
    LOGGER.info("add Element: {}", e);
    updateAll();
    return add;
  }

  @Override
  public int size() {
    int size = super.size();
    LOGGER.info("Size: {}", size);
    return size;
  }

  @Override
  public E get(int index) {
    LOGGER.info("Get: {} ", index);
    return super.get(index);
  }

  private List<Observer> observers = new ArrayList<>();

  public void updateAll() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  public void addListener(Observer observer) {
    observers.add(observer);
  }

  public void removeListener(Observer observer) {
    observers.remove(observer);
  }
}
