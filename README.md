# AEM Environment Badge

The environment badge is a simple helper on the AEM authoring UI used to indicate the current program and environment of
the AEM instance which is being browsed, authored or configured. This is especially useful for multi-program setups as
well as for programs with multiple non-production environments (DEV and RDE).

## Status

<p align="center">
    <a href="https://maven-badges.sml.io/sonatype-central/com.merkle.oss.aem/aem-environment-badge/">
        <img alt="Sonatype Central Version" src="https://img.shields.io/maven-central/v/com.merkle.oss.aem/aem-environment-badge?strategy=highestVersion&logo=sonatype&logoColor=white&logoSize=auto&label=sonatype-central&color=blue&link=https%3A%2F%2Fmaven-badges.sml.io%2Fsonatype-central%2Fcom.merkle.oss.aem%2Faem-environment-badge%2F"></a>
    <a href="https://javadoc.io/doc/com.merkle.oss.aem/aem-environment-badge">
        <img alt="Javadoc" src="https://javadoc.io/badge2/com.merkle.oss.aem/aem-environment-badge/javadoc.svg?color=yellow"></a>
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Quality Gate" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=alert_status"></a>
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Security Rating" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=security_rating"></a>
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Reliability Rating" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=reliability_rating"></a>
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Maintainability Rating" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=sqale_rating"></a>
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Code Coverage" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=coverage"></a>    
    <a href="https://sonarcloud.io/summary/overall?id=merkle-open_aem-environment-badge&branch=master">
        <img alt="SonarQube - Vulnerabilities" src="https://sonarcloud.io/api/project_badges/measure?project=merkle-open_aem-environment-badge&metric=vulnerabilities"></a>
    <a href="https://github.com/merkle-open/aem-environment-badge/actions/workflows/verify-snapshot.yml">
        <img alt="CI SNAPSHOT - Github Action" src="https://img.shields.io/github/actions/workflow/status/merkle-open/aem-environment-badge/verify-snapshot.yml?branch=develop&logo=githubactions&logoColor=white&logoSize=auto&label=ci-snapshot&link=https%3A%2F%2Fgithub.com%2Fmerkle-open%2Faem-utils%2Factions%2Fworkflows%2Fverify-snapshot.yml"></a>
    <a href="https://github.com/merkle-open/aem-environment-badge/actions/workflows/deploy-snapshot.yml">
        <img alt="Deploy SNAPSHOT - Github Action" src="https://img.shields.io/github/actions/workflow/status/merkle-open/aem-environment-badge/deploy-snapshot.yml?branch=develop&logo=githubactions&logoColor=white&logoSize=auto&label=deploy-snapshot&link=https%3A%2F%2Fgithub.com%2Fmerkle-open%2Faem-utils%2Factions%2Fworkflows%2Fdeploy-snapshot.yml"></a>
</p>

## Features

If enabled, the environment badge will be rendered within the AEM authoring UI as well as the CRX explorer (*only the
bar*).

### Badge

If configured, a badge is displayed with the configured title and background color. In addition to the badge, a bar
will be rendered on top of the page (*confined to the unified shell*) to indicate the current environment. The badge and
bar also work with the unified shell deactivated.
If no badge title has been configured, the badge will not be rendered, leaving only the bar visible to the authors.

#### Colors

![Background colors](docs/background-colors.png)

### Document title prefix

If configured, the document title will be prefixed with the configured string, resulting in an identifiable browser tab
title like `<PREFIX> | <BROWSER TITLE>`.

### Kudos

> [!NOTE]
> This project is inspired by the [ACS AEM Commons](https://adobe-consulting-services.github.io/acs-aem-commons/)
> project's [AEM Environment Indicator](https://adobe-consulting-services.github.io/acs-aem-commons/features/environment-indicator/index.html).

## Maven dependency

1. Add the `aem-environment-badge.all` artifact to the `<dependencies>` section

   ```xml
   <dependency>
     <groupId>com.merkle.oss.aem</groupId>
     <artifactId>aem-environment-badge.all</artifactId>
     <version>1.0.0</version>
     <type>zip</type>
   </dependency>
   ```
2. Embed the package in with
   the [filevault-package-maven-plugin](https://jackrabbit.apache.org/filevault-package-maven-plugin/) in
   the `<embeddeds>` section

   ```xml
   <embedded>
      <groupId>com.merkle.oss.aem</groupId>
      <artifactId>aem-environment-badge.all</artifactId>
      <target>/apps/{project/path/definition}/install</target>
   </embedded>
   ```

## AEM configuration

Define the OSGI config scoped to your desired AEM Author environment via:
`com.merkle.oss.aem.environmentbadge.services.impl.AEMEnvironmentBadgeConfigServiceImpl.cfg.json`

with example config for the RDE tier:

```
{
  "enableDocumentTitlePrefix": true,
  "documentTitlePrefix": "$[env:AEM_ENV_BADGE_DOC_TITLE_PREFIX;default=RDE]",
  "enableBadge": true,
  "badgeTitle": "$[env:AEM_ENV_BADGE_TITLE;default=rde]",
  "badgeBackgroundColor": "fuchsia"
}
```

*by defining titles via environment variables, environments of the same tier within the same program can be configured
to individualized titles and thereby exceed the capeabilites of the
OOTB [unified shell indicator](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/overview/aem-cloud-service-on-unified-shell#identify-aemaacs-environment).*

| Property                     | Description                                                                                                                             | Default   |
|------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|-----------|
| Enable document title prefix | Whether to prepend a prefix to the document title (browser tab)                                                                         | `false`   |
| Document title prefix        | The string prefix to be prepended to the browser tab title.<br/>*E.g.*: `<PREFIX> \| <BROWSER TITLE>`                                   | -         |
| Enable environment badge     | Whether the visual badge component and bar component should be rendered in the AEM UI                                                   | `false`   |
| Badge title                  | The text content displayed on the environment badge                                                                                     | -         |
| Background color             | The color string defining the badge's background color.<br/>*Options*:`red`,`blue`,`green`,`orange`,`grey`,`yellow`,`seafoam`,`fuchsia` | `fuchsia` |

## Development

Build locally with Maven

```
    mvn clean install -PautoInstallBundle
```

Build locally with Maven and deploy to AEM Author

```
    mvn clean install -PautoInstallPackage
```
