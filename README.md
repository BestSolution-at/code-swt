# SWT Code-Editor Framework

e(fx)clipse provides a low level framework to implement editors on top if the Eclipse Text Framework 
but it only provides native UI components for JavaFX.

The SWT Code-Editor Framework reuses the platform neutral bits from the e(fx)clipse code base and implements the SWT UI 
ontop of it.

# Dev Setup

## Prerequisite

* Download Eclipse Mars.1 or later from http://www.eclipse.org/downloads/
 * install EGit if not contained already
 * (Optional) if you want to write custom language bindings install the e(fx)clipse nightly tooling from http://download.eclipse.org/efxclipse/updates-nightly/site
* Maven3 installed or m2e inside your IDE
* (Optional) Dart SDK from https://www.dartlang.org/downloads/archive/ if you want to try out enhanced Dart features (eg auto-complete)
and extract it somewhere on your hard drive

## Source repositories

You need to clone:
* This repository - https://github.com/BestSolution-at/code-swt.git
* Dart Edit - https://github.com/BestSolution-at/dartedit.git - if you want to try enhanced Dart features
* (Optional) Syntax Highlighting - https://git.eclipse.org/r/efxclipse/org.eclipse.efxclipse - if you want to add highlightings for not yet supported languages

## Import Sources
* Import everything from `code-swt.git` into your workspace
* Import the following projects from `dartedit.git`
 * `at.bestsolution.dart.editor.services`
 * `at.bestsolution.dart.editor.services.e4`
 * `at.bestsolution.dart.server.api`
* (Optional) Import the following project from `org.eclipse.efxclipse`
 * `org.eclipse.fx.code.editor.langs`
 * `org.eclipse.fx.code.editor.langs.contrib`

## Target Platform setup
* Run the `mvn clean package` in `at.bestsolution.code.e4.swt.target.releng`
* Open the `code.target` in `at.bestsolution.code.e4.swt.target.updatesite`
* Click the `Set as Target Platform` link in the upper right corner of the editor

## Running application

### Only syntax highlighting

* Open the `at.bestsolution.code.app.product` file in the project named as well `at.bestsolution.code.app.product`
* Click the `Launch an Eclipse application` link
* Run the `New Project` action found in `File`-Menu and name the project `Sample`
* Select the project in the Viewer
* Run the `New File` action found next to the `New Project` and name the file `Sample.dart`
* Open the file

### Add Enhanced Dart features (if you have the Dart SDK installed)

* Open the launch configuration dialog for the product and navigate to the `Arguments`-Tab
* Add a System-Property in the VM-arguments Text-Area `-Ddart.sdkdir=PATH_TO_EXTRACTED_DART_SDK`
* Open the `Plug-ins`-Tab and add check the `at.bestsolution.code.dart.feature`
* Click the `Run`-Button
