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
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/vader/jquery-ui.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/manager.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/activity/default.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/manager.js"/>
			<SCRIPT src="{@contextPath}/activity/default.js"/>
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
				<!--<xsl:apply-templates select="search"/>-->
				<FORM id="pagination" class="contentWrapper" action="{concat(@contextPath,search/@action)}" method="get">
					<INPUT name="name" placeholder="活動名稱" type="text" value="{search/@name}"/>
					<I>&#160;</I>
					<INPUT class="sixty4" name="headcount" placeholder="人數" type="text" value="{search/@headcount}"/>
					<I>&#160;</I>
					<INPUT class="sixty4" name="score" placeholder="價值點數" type="text" value="{search/@score}"/>
					<I>&#160;</I>
					<LABEL title="當天或之後">
						<SPAN>開始日期：</SPAN>
						<INPUT class="dP ninety6" name="since" readonly="" type="text" value="{search/@since}"/>
					</LABEL>
					<I>&#160;</I>
					<LABEL title="之前或當天">
						<SPAN>結束日期：</SPAN>
						<INPUT class="dP ninety6" name="until" readonly="" type="text" value="{search/@until}"/>
					</LABEL>
					<I>&#160;</I>
					<INPUT class="thirty2" maxlength="2" name="age" placeholder="年齡" type="text" value="{search/@age}"/>
					<I>&#160;</I>
					<LABEL>
						<INPUT name="gender" type="radio" value="false"/>
						<SPAN>女性</SPAN>
					</LABEL>
					<LABEL>
						<INPUT name="gender" type="radio" value="true"/>
						<SPAN>男性</SPAN>
					</LABEL>
					<I>&#160;</I>
					<SELECT name="occupation">
						<OPTION value="">(職業)</OPTION>
						<xsl:apply-templates select="search/occupation/*"/>
					</SELECT>
					<I>&#160;</I>
					<SELECT name="district">
						<OPTION value="">(行政區)</OPTION>
						<xsl:apply-templates select="search/district/*"/>
					</SELECT>
					<I>&#160;</I>
					<xsl:if test="search/@previous">
						<xsl:if test="search/@first">
							<A class="button fontAwesome paginate" title="第一頁" tabindex="{search/@first}">&#xF049;</A>
						</xsl:if>
						<A class="button fontAwesome paginate" title="上一頁" tabindex="{search/@previous}">&#xF04A;</A>
					</xsl:if>
					<SPAN style="margin:0 3px">
						<LABEL>
							<SPAN>每頁</SPAN>
							<INPUT maxlength="2" name="s" type="text" value="{search/@size}"/>
							<SPAN>筆</SPAN>
						</LABEL>
						<SPAN>：</SPAN>
						<LABEL>
							<SPAN>第</SPAN>
							<SELECT name="p">
								<xsl:apply-templates select="search/pages/*"/>
							</SELECT>
							<SPAN>&#47;</SPAN>
							<SPAN>
								<xsl:value-of select="search/@totalPages"/>
							</SPAN>
							<SPAN>頁</SPAN>
						</LABEL>
						<SPAN>&#40;共</SPAN>
						<SPAN>
							<xsl:value-of select="search/@totalElements"/>
						</SPAN>
						<SPAN>筆&#41;</SPAN>
					</SPAN>
					<xsl:if test="search/@next">
						<A class="button fontAwesome paginate" title="下一頁" tabindex="{search/@next}">&#xF04E;</A>
						<xsl:if test="search/@last">
							<A class="button fontAwesome paginate" title="最後頁" tabindex="{search/@last}">&#xF050;</A>
						</xsl:if>
					</xsl:if>
					<INPUT style="display:none" type="submit"/>
				</FORM>
			</SECTION>
			<ARTICLE class="contentWrapper">
				<NAV>
					<xsl:apply-templates select="navigator">
						<xsl:with-param name="contextPath" select="@contextPath"/>
					</xsl:apply-templates>
				</NAV>
				<DIV id="contentWrapper">
					<xsl:apply-templates select="table"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="table">
		<xsl:param name="contextPath" select="contextPath"/>
		<TABLE class="list">
			<THEAD>
				<TR>
					<TH>索引</TH>
					<TH>活動名稱</TH>
					<TH>人數</TH>
					<TH>價值點數</TH>
					<TH>開始時戳</TH>
					<TH>結束時戳</TH>
					<TH>年齡需求</TH>
					<TH>性別需求</TH>
					<TH>職業需求</TH>
					<TH>行政區需求</TH>
					<TH>修改</TH>
					<TH>發送簡訊通知</TH>
				</TR>
			</THEAD>
			<TBODY>
				<xsl:for-each select="row">
					<TR>
						<xsl:if test="position()mod'2'='0'">
							<xsl:attribute name="class">odd</xsl:attribute>
						</xsl:if>
						<TD class="textAlignCenter">
							<xsl:value-of select="id"/>
						</TD>
						<TD>
							<xsl:value-of select="name"/>
						</TD>
						<TD class="textAlignCenter">
							<xsl:value-of select="headcount"/>
						</TD>
						<TD class="textAlignCenter">
							<xsl:value-of select="score"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="since"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="until"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:if test="minimumBirth">
								<xsl:attribute name="title">
									<xsl:value-of select="minimumBirth"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="age"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:if test="gender='false'">
								<SPAN class="fontAwesome" style="color:#FF8040">&#xF182;</SPAN>
							</xsl:if>
							<xsl:if test="gender='true'">
								<SPAN class="fontAwesome" style="color:#4080FF">&#xF183;</SPAN>
							</xsl:if>
						</TD>
						<TD>
							<xsl:value-of select="occupation"/>
						</TD>
						<TD>
							<xsl:value-of select="district"/>
						</TD>
						<TD class="textAlignCenter">
							<xsl:if test="editable='true'">
								<A class="fontAwesome" href="{id}.asp">&#xF14B;</A>
							</xsl:if>
						</TD>
						<TD class="textAlignCenter">
							<xsl:if test="notify='true'">
								<A class="ajax fontAwesome" href="{id}/notify.json">&#xF098;</A>
							</xsl:if>
						</TD>
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
	</xsl:template>

</xsl:stylesheet>