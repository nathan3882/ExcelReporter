package me.nathan3882.parsing.responding;

import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.AccessLevel;
import lombok.Getter;
import me.nathan3882.parsing.IParsable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.LinkedList;


@Getter(AccessLevel.PROTECTED)
public abstract class ParseResponse<T> implements IParsable {

    private static final Logger RESPONDER_LOGGER;

    static {
        RESPONDER_LOGGER = LoggerFactory.getLogger(ParseResponse.class);
    }

    protected final Class<T> dataIsOfThisClass;
    @Getter(AccessLevel.NONE) //subclass must provide their own implementation
    protected T[] responseData;


    ParseResponse(Class<T> dataIsOfThisClass, T... data) throws InvalidArgumentException {
        this.dataIsOfThisClass = dataIsOfThisClass;
        int dataLength = data.length;

        if (dataLength > 0) { //data is iterable
            responseData = (T[]) Array.newInstance(dataIsOfThisClass, dataLength);

            for (int i = 0; i < data.length; i++) {
                T datum = data[i];
                responseData[i] = datum;
            }

        } else { //not iterable
            throw new InvalidArgumentException(new String[]{"Not enough data was supplied"});
        }

    }

    public abstract Boolean wasSuccessfull();

    public abstract T[] getResponseData();

    public LinkedList<String> getResponseDataAsList() {

    }

}
