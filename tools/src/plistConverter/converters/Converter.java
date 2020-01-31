package plistConverter.converters;

import plistConverter.models.old.Plist;

public interface Converter<T> {
    T convert(Plist plist);
    String getPath();
    String getInitialFolder();
    String getDestinationFolder();
}