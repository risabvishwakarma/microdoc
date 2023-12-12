package com.unitral.microdoc.enums;

import org.apache.tika.mime.MediaType;

public enum FileFormat {
    // Document formats
    WORD_DOCUMENT(MediaType.application("vnd.openxmlformats-officedocument.wordprocessingml.document")),
    PDF_DOCUMENT(MediaType.application("pdf")),
    // Image formats
    JPEG_IMAGE(MediaType.image("jpeg")),
    PNG_IMAGE(MediaType.image("png")),
    // Audio formats
    MP3_AUDIO(MediaType.audio("mpeg")),
    // Video formats
    MP4_VIDEO(MediaType.video("mp4")),


    // Generic application type
    GENERIC_APPLICATION(MediaType.application("*"));

    private final MediaType mediaType;

    FileFormat(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
