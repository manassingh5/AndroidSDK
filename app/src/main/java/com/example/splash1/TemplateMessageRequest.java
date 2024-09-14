package com.example.splash1;

public class TemplateMessageRequest {
    private String messaging_product;
    private String to;
    private String type;
    private Template template;

    public TemplateMessageRequest(String to) {
        this.messaging_product = "whatsapp";
        this.to = to;
        this.type = "template";
        this.template = new Template("hello_world", "en_US");
    }

    public static class Template {
        private String name;
        private Language language;

        public Template(String name, String code) {
            this.name = name;
            this.language = new Language(code);
        }

        public static class Language {
            private String code;

            public Language(String code) {
                this.code = code;
            }
        }
    }
}
