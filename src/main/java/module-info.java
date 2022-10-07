module file.worker {
    requires silly.io.main;
    requires throwable;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires checksum.up;
    requires crypt.api;
    requires java.logging;
    requires silly.collection.main;
    requires silly.strings;
    exports io.github.astrapi69.file;
    exports io.github.astrapi69.file.compare;
    exports io.github.astrapi69.file.compare.api;
    exports io.github.astrapi69.file.copy;
    exports io.github.astrapi69.file.create;
    exports io.github.astrapi69.file.csv;
    exports io.github.astrapi69.file.delete;
    exports io.github.astrapi69.file.exception;
    exports io.github.astrapi69.file.modify;
    exports io.github.astrapi69.file.modify.api;
    exports io.github.astrapi69.file.read;
    exports io.github.astrapi69.file.rename;
    exports io.github.astrapi69.file.search;
    exports io.github.astrapi69.file.sort;
    exports io.github.astrapi69.file.system;
    exports io.github.astrapi69.file.write;
    exports io.github.astrapi69.file.zip;
}
