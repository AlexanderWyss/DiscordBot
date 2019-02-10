package wyss.website.discordbot.music;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObservableList<E> extends ArrayList<E> {

  private static final long serialVersionUID = 2780525358637621254L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ObservableList.class);

  @Override
  public void add(int index, E element) {
    super.add(index, element);
    LOGGER.info("add Index: {} Element: {}", index, element);

  }

  @Override
  public boolean add(E e) {
    LOGGER.info("add Element: {}", e);
    return super.add(e);
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
}
