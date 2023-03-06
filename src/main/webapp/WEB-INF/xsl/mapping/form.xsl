<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		doctype-system="about:blank"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/common.xsl"/>

	<xsl:template match="/">
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<META content="width=device-width,initial-scale=1.0" name="viewport"/>
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/manager.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/manager.js"/>
			<TITLE>後臺 &#187; <xsl:value-of select="@title" disable-output-escaping="yes"/></TITLE>
		</HEAD>
		<xsl:comment>
			<xsl:value-of select="system-property('xsl:version')"/>
		</xsl:comment>
		<BODY>
			<HEADER>
				<DIV class="cF contentWrapper">
					<DIV class="fL">
						<A href="{@contextPath}/">寶盒計畫</A>
						<SPAN>&#187;</SPAN>
						<A>
							<xsl:value-of select="@title" disable-output-escaping="yes"/>
						</A>
					</DIV>
					<DIV class="fR">
						<A>
							<xsl:value-of select="@name"/>
						</A>
						<SPAN>&#187;</SPAN>
						<A href="{@contextPath}/bye.asp">登出</A>
					</DIV>
				</DIV>
			</HEADER>
			<SECTION>
				<xsl:apply-templates select="search"/>
			</SECTION>
			<ARTICLE class="contentWrapper">
				<NAV>
					<xsl:apply-templates select="navigator">
						<xsl:with-param name="contextPath" select="@contextPath"/>
					</xsl:apply-templates>
				</NAV>
				<DIV id="contentWrapper">
					<xsl:apply-templates select="form"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="POST">
			<FIELDSET>
				<TABLE class="fieldset">
					<CAPTION>
						<xsl:value-of select="errorMessage"/>
					</CAPTION>
					<TBODY>
						<TR>
							<TH class="must">
								<LABEL for="accordion">手風琴</LABEL>
							</TH>
							<TD>
								<SELECT id="accordion" name="accordion">
									<xsl:apply-templates select="accordion/*"/>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<TH>
								<LABEL for="sort">排序</LABEL>
							</TH>
							<TD>
								<INPUT id="sort" name="sort" type="text" value="{sort}"/>
							</TD>
						</TR>
						<TR>
							<TH>
								<LABEL for="title">標題</LABEL>
							</TH>
							<TD>
								<INPUT id="title" name="title" type="text" value="{title}"/>
							</TD>
						</TR>
						<TR>
							<TH>
								<LABEL for="uri">路徑</LABEL>
							</TH>
							<TD>
								<INPUT id="uri" name="uri" type="text" value="{uri}"/>
							</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="method">方式</LABEL>
							</TH>
							<TD>
								<SELECT id="method" name="method">
									<xsl:apply-templates select="method/*"/>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="pattern">規則</LABEL>
							</TH>
							<TD>
								<INPUT id="pattern" name="pattern" type="text" value="{pattern}"/>
							</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="description">描述</LABEL>
							</TH>
							<TD>
								<INPUT id="description" name="description" type="text" value="{description}"/>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				<P class="cF">
					<INPUT class="fL" type="reset" value="復原"/>
					<INPUT class="fR" type="submit" value="確定"/>
				</P>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>