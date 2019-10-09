package wbif.sjx.common.Object.Volume;

public class PointOutOfRangeException extends Throwable {
    public PointOutOfRangeException() {
    }

    public PointOutOfRangeException(String message) {
        super(message);
    }

    public PointOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointOutOfRangeException(Throwable cause) {
        super(cause);
    }

    public PointOutOfRangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
