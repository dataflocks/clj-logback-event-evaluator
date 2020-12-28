# clj-logback-event-evaluator

![CI](https://github.com/dataflocks/clj-logback-event-evaluator/workflows/CI/badge.svg?branch=main)

A logback [EventEvaluator](http://logback.qos.ch/manual/filters.html#evalutatorFilter) that evaluates clojure expressions.

In projects where clojure is already a dependency it helps avoid an additional
dependency on eg Janino when dynamic filtering of log events is desired.

```
*** Warning ***

The expressions are eval'd and there is currently no sandboxing in place. Please
take into consideration for your specific use case and proceed at your own risk!
```

## Usage

Once it's added your project dependencies it can be used like any other
`EventEvaluator`. Here's a translation of a [Janino example](http://logback.qos.ch/manual/filters.html#JaninoEventEvaluator):

```xml
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
        <evaluator class="dataflocks.CljLogbackEventEvaluator">
	  <expression>(.contains msg "billing")</expression>
	</evaluator>
        <OnMismatch>NEUTRAL</OnMismatch>
        <OnMatch>DENY</OnMatch>
    </filter>
    <encoder>
      <pattern>
        %-4relative [%thread] %-5level %logger - %msg%n
      </pattern>
    </encoder>
  </appender>

  <root level="INFO">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

### Sugar

The Evaluator provides access to the [ILoggingEvent](https://logback.qos.ch/apidocs/ch/qos/logback/classic/spi/ILoggingEvent.html) via `event`,
as well as to some of it's members via the mapping in [dataflocks.clj-logback-event-evaluator](src/dataflocks/clj_logback_event_evaluator.clj#L12)

In a Evaluator Expression, this should hold for example:

```clojure
(= msg (.getMessage event))
```

## License

```
MIT License

Copyright © 2020 Maximilian Karasz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```