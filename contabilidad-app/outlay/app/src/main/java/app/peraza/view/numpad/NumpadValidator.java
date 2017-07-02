package app.peraza.view.numpad;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public interface NumpadValidator {
    boolean valid(String value, String curr);

    void onInvalidInput(String value);
}
