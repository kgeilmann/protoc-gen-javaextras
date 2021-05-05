package com.github.kgeilmann.javaprotocextras;

import com.github.kgeilmann.javaprotocextras.getters.GetTimestampAsInstantGenerator;
import com.github.kgeilmann.javaprotocextras.setters.SetOrClearGenerator;
import com.github.kgeilmann.javaprotocextras.setters.UnwrappedSetterGenerator;
import com.salesforce.jprotoc.Generator;
import com.salesforce.jprotoc.ProtocPlugin;

import java.util.List;

public class JavaExtrasPlugin {

    public static void main(String[] args) {
        final List<Generator> all = List.of(new SetOrClearGenerator(), new UnwrappedSetterGenerator(),
                new GetTimestampAsInstantGenerator());

        if (args.length > 0) {
            ProtocPlugin.debug(all, args[0]);
        } else {
            ProtocPlugin.generate(all);
        }
    }
}
