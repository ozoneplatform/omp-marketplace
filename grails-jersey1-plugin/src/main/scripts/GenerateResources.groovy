/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.grails.cli.interactive.completers.DomainClassCompleter

description("Generates JAX-RS resource and CRUD service classes for domain classese") {
    usage "grails generate-resources [DOMAIN CLASS]"
    argument name: 'Domain Class', description: "The fully-qualified name of a domain class to generate sources for", required: true
    completer DomainClassCompleter
    flag name: 'force', description: "Whether to overwrite existing files"
}

if (!args) {
    error "No domain class(es) specified"
}

boolean force = flag('force')

def classNames = args
if (args.size() == 1 && args[0] == '*') {
    classNames = resources("file:grails-app/domain/**/*.groovy").collect { className(it) }
}

classNames.each { className ->
    def sourceClass = source(className)

    if (!sourceClass) {
        error "Domain class not found for name $className"
        return
    }

    def model = model(sourceClass)

    render template: template('scaffolding/CollectionResource.groovy'),
        destination: file("grails-app/resources/${model.packagePath}/${model.convention('CollectionResource')}.groovy"),
        model: model,
        overwrite: force

    render template: template('scaffolding/Resource.groovy'),
        destination: file("grails-app/resources/${model.packagePath}/${model.convention('Resource')}.groovy"),
        model: model,
        overwrite: force

    render template: template('scaffolding/ResourceService.groovy'),
        destination: file("grails-app/services/${model.packagePath}/${model.convention('ResourceService')}.groovy"),
        model: model,
        overwrite: force

    addStatus "Scaffolding completed for ${projectPath(sourceClass)}"
}
