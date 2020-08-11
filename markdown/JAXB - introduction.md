# `JAXB` - `introduction`

**`The Java Architecture for XML Binding 2.0`**

![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200414/a80df1df9b41087353a151c6877dba9a.png_target)

## `introduction`

> XML is, essentially, a platform-independent means of structuring information. An XML document is a tree of elements. An element may have a set of attributes, in the form of key-value pairs, and may contain other elements, text, or a mixture thereof. An element may refer to other elements via identifierattributes or other types via type attributes, thereby allowing arbitrary graph structures to be represented. 

XML本质上是一种独立于平台的信息结构化方法。XML文档是元素树。一个元素可以有一组属性(以键-值对的形式)，也可以包含其他元素、文本或它们的混合。元素可以通过标识符属性引用其他元素，也可以通过类型属性引用其他类型，因此可以表示任意的图结构。

> An XML document need not follow any rules beyond the well-formedness criteria laid out in the XML 1.0 specification. To exchange documents in a meaningful way, however, requires that their structure and content be described and constrained so that the various parties involved will interpret them correctly and consistently. This can be accomplished through the use of a schema. A schema contains a set of rules that constrains the structure and content of a document’s components, i.e., its elements, attributes, and text. A schema also describes, at least informally and often implicitly, the intended conceptual meaning of a document’s components. A schema is, in other words, a specification of the syntax and semantics of a (potentially infinite) set of XML documents. A document is said to be valid with respect to a schema if, and only if, it satisfies the constraints specified in the schema.

XML文档除了遵循XML 1.0规范中规定的格式标准外，无需遵循任何规则。 但是，以有意义的方式交换文档需要描述和约束文档的结构和内容，以便相关各方可以正确，一致地解释它们。 这可以通过使用模式来完成。 模式包含一组规则，这些规则约束文档组件的结构和内容，即其元素，属性和文本。 模式还至少非正式地（通常是隐式）描述了文档组件的预期概念含义。 换句话说，模式是XML文档集（可能无限）的语法和语义的规范。 当且仅当文档满足模式中指定的约束时，文档才对模式有效。

> In what language is a schema defined? The XML specification itself describes a sublanguage for writing document-type definitions, or DTDs. As schemas go, however, DTDs are fairly weak. They support the definition of simple constraints on structure and content, but provide no real facility for expressing datatypes or complex structural relationships. They have also prompted the creation of more sophisticated schema languages such as XDR, SOX, RELAX, TREX, and, most significantly, the XML Schema language defined by the World Wide Web Consortium. The XML Schema language has gained widespread acceptance. It is the schema language of choice for many of the XML related specifications authored by industry standard working groups. Therefore, the design center for this specification is W3C XML Schema language.

模式用什么语言定义？ XML规范本身描述了用于编写文档类型定义或DTD的子语言。 然而，随着模式的发展，DTD相当薄弱。 它们支持对结构和内容的简单约束的定义，但不提供表示数据类型或复杂结构关系的真正工具。 他们还促使创建更复杂的模式语言，例如XDR，SOX，RELAX，TREX，最重要的是，创建了万维网联盟定义的XML模式语言。 XML Schema语言已获得广泛接受。 它是行业标准工作组编写的许多XML相关规范的首选模式语言。 因此，此规范的设计中心是W3C XML Schema语言。

## 1. `Data binding`
> Any nontrivial application of XML will, then, be based upon one or more schemas and will involve one or more programs that create, consume, and manipulate documents whose syntax and semantics are governed by those schemas. While it is certainly possible to write such programs using the lowlevel SAX parser API or the somewhat higher-level DOM parse-tree API, doing so is not easy. The resulting code is also difficult to maintain as the schemas evolve. While useful to some, many applications access and manipulate XML content within a document; its document structure is less relevant.

因此，任何不平凡的XML应用程序都将基于一个或多个模式，并将涉及一个或多个程序，这些程序创建，使用和操作其语法和语义受这些模式控制的文档。 虽然肯定可以使用低级SAX解析器API或某种较高级别的DOM解析树API编写此类程序，但这并不容易。 随着模式的发展，生成的代码也难以维护。 尽管对某些应用程序有用，但许多应用程序都可以访问和操作文档中的XML内容。 它的文档结构不那么相关。

> It would be much easier to write XML-enabled programs if we could simply map the components of an XML document to in-memory objects that represent, in an obvious and useful way, the document’s intended meaning according to its schema. Of what classes should these objects be instances? In some cases there will be an obvious mapping from schema components to existing classes, especially for common types such as String, Date, Vector, and so forth. In general, however, classes specific to the schema being used will be required.

如果我们可以简单地将XML文档的组件映射到内存中的对象，这些对象将以一种明显且有用的方式根据其架构表示文档的预期含义，那么编写支持XML的程序将容易得多。 这些对象应属于哪些类的实例？ 在某些情况下，从架构组件到现有类的映射很明显，尤其是对于诸如String，Date，Vector等常见类型的映射。 但是，一般而言，将需要特定于所使用架构的类。

> Classes specific to a schema may be derived or may already exist. In some application scenarios e.g. web services, a data model composed using user authored classes may already exist. A schema is derived from existing classes. In-memory objects are instances of existing classes. In other application scenarios, a data model is composed by authoring a schema. In such cases, rather than burden developers with having to write classes specific to schema, we can generate the classes directly from the schema. In all application scenarios, we create a Java object-level binding of the schema.

特定于模式的类可能派生或已经存在。 在某些应用场景中 Web服务，使用用户创作的类组成的数据模型可能已经存在。 模式是从现有类派生的。 内存中对象是现有类的实例。 在其他应用场景中，数据模型是通过编写模式来构成的。 在这种情况下，我们可以直接从模式中生成类，而不必让开发人员不得不编写特定于模式的类。 在所有应用程序场景中，我们创建模式的Java对象级绑定。

> But even applications manipulating documents at conceptual object level, may desire to access or manipulate structural information in a document, e.g. element names. Therefore, the ability for an application to relate between the XML document representation and the Java object-level binding enables the use of XML operations over the XML document representation, e.g. Xpath.to bind XML content to an object model such as DOM is useful.

但是，即使是在概念对象级别上操纵文档的应用程序，也可能希望访问或操纵文档中的结构信息，例如 元素名称。 因此，应用程序能够在XML文档表示形式与Java对象级绑定之间建立关联的能力，使得可以在XML文档表示形式上使用XML操作，例如，XML文档。 Xpath。将XML内容绑定到对象模型（例如DOM）很有用。

> An XML data-binding facility therefore contains a schema compiler and a schema generator. A schema compiler can consume a schema and generate schema derived classes specific to the schema. A schema generator consumes a set of existing classes and generates a schema.

因此，XML数据绑定功能包含一个模式编译器和一个模式生成器。 模式编译器可以使用模式并生成特定于该模式的模式派生类。 模式生成器使用一组现有的类并生成一个模式。

> A schema compiler binds components of a source schema to schema-derived Java value classes. Each value class provides access to the content of the corresponding schema component via a set of JavaBeans-style access (i.e., getand set) methods. Binding declarations provides a capability to customize the binding from schema components to Java representation.

模式编译器将源模式的组件绑定到模式派生的Java值类。 每个值类都通过一组JavaBeans风格的访问（即get 和 set）方法提供对相应模式组件内容的访问。 绑定声明提供了自定义从架构组件到Java表示的绑定的功能。

> A schema generator binds existing classes to schema components. Each class containing Java Beans-style access (i.e., get and set) methods is bound to a corresponding schema component. Program annotations provide a capability to customize the binding from existing classes to derived schema components. Access to content is through in-memory representation of existing classes.

模式生成器将现有的类绑定到模式组件。 每个包含Java Beans样式访问（即get和set）方法的类都绑定到相应的架构组件。 程序批注提供了自定义从现有类到派生架构组件的绑定的功能。 通过现有类的内存表示形式可以访问内容。

> Such a facility also provides a binding framework, a runtime API that, in conjunction with the derived or existing classes, supports three primary operations:
>  ● The unmarshalling of an XML document into a tree of interrelated instances of both existing and schema-derived classes,
>  ● The marshalling of such content trees back into XML documents, and
>  ● The binding, maintained by a binder, between an XML document representation and content tree.
> The unmarshalling process has the capability to check incoming XML documents for validity with respect to the schema.

这种功能还提供了一个绑定框架，一个运行时 API，该 API 与派生类或现有类一起支持三个主要操作：
* 将XML文档解组为现有类和模式派生类的相互关联实例的树，
* 将此类内容树编组回XML文档，并且
* XML文档表示形式和内容树之间的绑定（由绑定程序维护）。
解组过程具有检查传入XML文档相对于模式有效性的能力。

![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200414/37de74d4e4f08a0ee8eca623d2686ed5.png_target)

### Binding XML to Java objects
> To sum up: Schemas describe the structure and meaning of an XML document, in much the same way that a class describes an object in a program. To work with an XML document in a program we would like to map its components directly to a set of objects that reflect the document’s meaning according to its schema. We can achieve this by compiling the schema into a set of derived content classes or by compiling a set of existing classes into a schema and marshalling, unmarshalling and validating XML documents that follow the schema. Data binding thus allows XML-enabled programs to be written at the same conceptual level as the documents they manipulate, rather than at the more primitive level of parser events or parse trees.

概括起来：模式描述XML文档的结构和含义，与类描述程序中的对象的方式几乎相同。 为了在程序中使用XML文档，我们希望将其组件直接映射到一组对象，这些对象根据其模式反映了文档的含义。 我们可以通过将架构编译为一组派生的内容类，或将一组现有类编译为架构，然后对遵循该架构的XML文档进行编组，解组和验证来实现此目的。 因此，数据绑定使启用XML的程序可以在与其操作的文档相同的概念级别上编写，而不是在解析器事件或解析树的更原始级别上编写。

> Schema evolution in response to changing application requirements, is inevitable. A document therefore might not always necessarily follow the complete schema, rather a part of a versioned schema. Dealing with schema evolution requires both a versioning strategy as well as more flexible marshalling, unmarshalling and validation operations.

响应不断变化的应用程序需求而进行模式演变是不可避免的。 因此，文档可能不一定总是遵循完整的架构，而是版本化架构的一部分。 处理模式演变既需要版本控制策略，也需要更灵活的编组，解组和验证操作。

> XML applications, such as workflow applications, consist of distributed, cooperative components interoperating using XML documents for data 
> exchange. Each distributed component may need to access and update only a part of the XML document relevant to itself, while retaining the fidelity of the rest of the XML document. This is also more robust in the face of schema evolution, since the changes in schema may not impact the part of the document relevant to the application. The binder enables partial binding of the relevant parts of the XML document to a content tree and marshalling updates back to the original XML document.

XML应用程序(如工作流应用程序)由使用XML文档进行数据交换的分布式协作组件组成。每个分布式组件可能只需要访问和更新与自身相关的XML文档的一部分，同时保持XML文档其余部分的保真度。在面对模式演化时，这也更加健壮，因为模式中的更改可能不会影响与应用程序相关的文档部分。绑定器支持将XML文档的相关部分部分绑定到内容树，并对更新进行编组。