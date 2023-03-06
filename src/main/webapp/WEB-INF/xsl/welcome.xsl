<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		doctype-system="about:blank"
		indent="no"
		media-type="text/html"
	/>

	<xsl:template match="/">
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<LINK href="{@contextPath}/welcome.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="https://apis.google.com/js/client:platform.js" async="" defer=""/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="{@contextPath}/welcome.js"/>
			<TITLE>寶盒計畫</TITLE>
		</HEAD>
		<xsl:comment>
			<xsl:value-of select="system-property('xsl:version')"/>
		</xsl:comment>
		<BODY>
			<DIV id="fb-root"></DIV>
			<HEADER>寶盒計畫</HEADER>
			<FOOTER>
				<DIV class="authentication" id="login">
					<BUTTON id="facebook">使用<IMG alt="Facebook" src="IMG/facebook.png" width="29"/>登入</BUTTON>
				</DIV>
				<DIV class="authentication" id="signin">
					<BUTTON id="google">使用<IMG alt="Google+" src="IMG/google.png" width="29"/>登入</BUTTON>
				</DIV>
			</FOOTER>
		</BODY>
	</xsl:template>

</xsl:stylesheet>