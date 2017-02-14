package com.ctrip.ops.sysdev.test;


import java.util.HashMap;
import java.util.Map;

import com.ctrip.ops.sysdev.filters.Replace;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

public class TestReplace {
    @Test
    public void testReplace() {
        String c = String
                .format("%s\n%s\n",
                        "src: name",
                        "value: abcd");
        Yaml yaml = new Yaml();
        Map config = (Map) yaml.load(c);
        Assert.assertNotNull(config);

        Replace filter = new Replace(config);
        Map<String, Object> event = new HashMap();
        event.put("name", "xyz");
        event = filter.process(event);

        Assert.assertEquals(event.get("name"), "abcd");

        c = String
                .format("%s\n%s\n",
                        "src: name",
                        "value: ${nick}");
        yaml = new Yaml();
        config = (Map) yaml.load(c);
        Assert.assertNotNull(config);

        filter = new Replace(config);
        event.clear();
        event.put("name", "xyz");
        event.put("nick", "abcd");
        event = filter.process(event);

        Assert.assertEquals(event.get("name"), "abcd");


        c = String
                .format("%s\n%s\n",
                        "src: name",
                        "value: '[nick]'");
        yaml = new Yaml();
        config = (Map) yaml.load(c);
        Assert.assertNotNull(config);

        filter = new Replace(config);
        event.clear();
        event.put("name", "xyz");
        event.put("nick", "abcd");
        event = filter.process(event);

        Assert.assertEquals(event.get("name"), "abcd");


        c = String
                .format("%s\n%s\n",
                        "src: name",
                        "value: '[nick][first]'");
        yaml = new Yaml();
        config = (Map) yaml.load(c);
        Assert.assertNotNull(config);

        filter = new Replace(config);
        event.clear();
        event.put("name", "xyz");
        event.put("nick", new HashMap() {{
            this.put("first", "c");
            this.put("last", "d");
        }});
        event = filter.process(event);

        Assert.assertEquals(event.get("name"), "c");


        c = String
                .format("%s\n%s\n",
                        "src: name",
                        "value: '[nick]'");
        yaml = new Yaml();
        config = (Map) yaml.load(c);
        Assert.assertNotNull(config);

        filter = new Replace(config);
        event.clear();
        event.put("name", "xyz");
        event.put("nick", new HashMap() {{
            this.put("first", "c");
            this.put("last", "d");
        }});
        event = filter.process(event);

        Assert.assertEquals(((Map) event.get("name")).get("first"), "c");
        Assert.assertEquals(((Map) event.get("name")).get("last"), "d");
    }
}
