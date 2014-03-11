<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xhtml="http://www.w3.org/1999/xhtml">
    <!--

    Parst den Link zum Stundenplan aus der Dualis Startseite heraus.

    -->
    <xsl:template match="xhtml:html">
        <xsl:for-each select="//*/xhtml:div[@class='appMonth']/xhtml:a">
            <xsl:value-of select="attribute::title" /> \n
            <xsl:value-of select="attribute::href" /> \n
            <xsl:value-of select="." /> \n
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>