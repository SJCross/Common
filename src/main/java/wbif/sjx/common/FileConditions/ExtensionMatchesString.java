package wbif.sjx.common.FileConditions;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by Stephen on 24/10/2016.
 */
public class ExtensionMatchesString implements FileCondition {
    private String[] exts;
    private Mode mode;

    public ExtensionMatchesString(String ext) {
        this.exts = new String[]{ext};
        this.mode = Mode.INC_PARTIAL;
    }

    public ExtensionMatchesString(String ext, Mode mode) {
        this.exts = new String[]{ext};
        this.mode = mode;
    }

    public ExtensionMatchesString(String[] exts) {
        this.exts = exts;
        this.mode = Mode.INC_PARTIAL;
    }

    public ExtensionMatchesString(String[] exts, Mode mode) {
        this.exts = exts;
        this.mode = mode;
    }

    public boolean test(File file) {
        if (file != null) {
            String extension = FilenameUtils.getExtension(file.getName());

            for (String ext : exts) {
                switch (mode) {
                    case INC_COMPLETE:
                        if (extension.matches(ext)) return true;
                        break;
                    case INC_PARTIAL:
                        if (extension.contains(ext)) return true;
                        break;
                    case EXC_COMPLETE:
                        if (extension.matches(ext)) return false;
                        break;
                    case EXC_PARTIAL:
                        if (extension.contains(ext)) return false;
                        break;
                }
            }
        }

        switch (mode) {
            case INC_COMPLETE:
            case INC_PARTIAL:
            default:
                return false;
            case EXC_COMPLETE:
            case EXC_PARTIAL:
                return true;
        }
    }

    public String[] getExts() {
        return exts;
    }

    public Mode getMode() {
        return mode;
    }
}