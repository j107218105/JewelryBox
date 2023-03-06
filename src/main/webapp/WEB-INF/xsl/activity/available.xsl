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
			<TITLE>寶盒計畫 &#187; <xsl:value-of select="@title"/></TITLE>
		</HEAD>
		<BODY>
			<HEADER>
				<DIV class="cF contentWrapper">
					<DIV class="fL">
						<A href="{@contextPath}/">寶盒計畫</A>
						<SPAN>&#187;</SPAN>
						<A>
							<xsl:value-of select="@title"/>
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
				<!--
				<FORM class="contentWrapper" action="" method="get">
					<A class="button fontAwesome paginate" title="第一頁" tabindex="1">&#xF049;</A>
					<A class="button fontAwesome paginate" title="前一頁" tabindex="2">&#xF04A;</A>
					<SELECT name="c">
						<OPTION value="">(查詢)</OPTION>
					</SELECT>
					<LABEL>
						<SPAN>每頁</SPAN>
						<INPUT maxlength="2" name="s" type="text" value="">
						<SPAN>筆</SPAN>
					</LABEL>
					<SPAN>：</SPAN>
					<LABEL>
						<SPAN>第</SPAN>
						<SELECT name="p">
							<OPTION value="1">1</OPTION>
						</SELECT>
						<SPAN>&#47;</SPAN>
						<SPAN>{@totalPage}</SPAN>
						<SPAN>頁</SPAN>
					</LABEL>
					<SPAN>(共</SPAN>
					<SPAN>{@count}</SPAN>
					<SPAN>筆)</SPAN>
					<A class="button fontAwesome paginate" title="下一頁" tabindex="4">&#xF04E;</A>
					<A class="button fontAwesome paginate" title="最後頁" tabindex="5">&#xF050;</A>
				</FORM>
				-->
			</SECTION>
			<ARTICLE class="contentWrapper">
				<NAV>
					<UL>
						<LI class="toggled">
							<A>會員</A>
							<UL>
								<LI>
									<A href="{@contextPath}/xxxxxx">待確認</A>
								</LI>
								<LI>
									<A href="{@contextPath}/applicant.asp">待撥款</A>
								</LI>
								<LI>
									<A href="{@contextPath}/blacklist.asp">黑名單</A>
								</LI>
								<LI>
									<A href="{@contextPath}/denied.asp">封鎖</A>
								</LI>
							</UL>
						</LI>
						<LI>
							<A>活動</A>
							<UL>
								<LI>
									<A href="{@contextPath}/activity/available.asp">未開始的活動</A>
								</LI>
								<LI>
									<A href="{@contextPath}/activity/underway.asp">進行中的活動</A>
								</LI>
								<LI>
									<A href="{@contextPath}/activity/finished.asp">已結束的活動</A>
								</LI>
								<LI>
									<A href="{@contextPath}/activity/add.asp">新增</A>
								</LI>
							</UL>
						</LI>
					</UL>
				</NAV>
				<DIV id="contentWrapper">
					<TABLE class="list">
						<THEAD>
							<TR>
								<TH>會員狀態</TH>
								<TH>年齡</TH>
								<TH>性別</TH>
								<TH>全名</TH>
								<TH>手機</TH>
								<TH>職業</TH>
								<TH>帳戶</TH>
								<TH>帳戶戶名</TH>
								<TH>帳戶帳號</TH>
								<TH>活動狀態</TH>
							</TR>
						</THEAD>
						<TBODY>
							<TR class="odd">
								<TD></TD>
								<TD>19</TD>
								<TD>女</TD>
								<TD>趙宥晴</TD>
								<TD class="bitstreamVeraSansMono">0963-096-225</TD>
								<TD>學生</TD>
								<TD>郵局</TD>
								<TD>趙宥晴</TD>
								<TD class="bitstreamVeraSansMono">O0413220536291</TD>
								<TD><A class="fontAwesome">&#xF017;</A></TD>
							</TR>
							<TR>
								<TD><A class="fontAwesome">&#xF088;</A></TD>
								<TD>28</TD>
								<TD>女</TD>
								<TD>林昱彤</TD>
								<TD class="bitstreamVeraSansMono">0975-318-261</TD>
								<TD>學生</TD>
								<TD>中國信託</TD>
								<TD>林昱彤</TD>
								<TD class="bitstreamVeraSansMono">037540408300</TD>
								<TD></TD>
							</TR>
							<TR class="odd">
								<TD></TD>
								<TD>19</TD>
								<TD>女</TD>
								<TD>趙宥晴</TD>
								<TD class="bitstreamVeraSansMono">0963-096-225</TD>
								<TD>學生</TD>
								<TD>郵局</TD>
								<TD>趙宥晴</TD>
								<TD class="bitstreamVeraSansMono">O0413220536291</TD>
								<TD><A class="fontAwesome">&#xF017;</A></TD>
							</TR>
							<TR>
								<TD><A class="fontAwesome">&#xF05E;</A></TD>
								<TD>28</TD>
								<TD>女</TD>
								<TD>林昱彤</TD>
								<TD class="bitstreamVeraSansMono">0975-318-261</TD>
								<TD>學生</TD>
								<TD>中國信託</TD>
								<TD>林昱彤</TD>
								<TD class="bitstreamVeraSansMono">037540408300</TD>
								<TD></TD>
							</TR>
						</TBODY>
					</TABLE>
					<FORM>
						<FIELDSET>
							<LEGEND>xxx</LEGEND>
							<TABLE>
								<TBODY>
									<TR>
										<TH>
											<LABEL for="column1">column1</LABEL>
										</TH>
										<TD>
											<INPUT id="column1" name="column1" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column2</TH>
										<TD>
											<INPUT name="column2" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
									<TR>
										<TH>column</TH>
										<TD>
											<INPUT name="column" type="text">
										</TD>
									</TR>
								</TBODY>
							</TABLE>
						</FIELDSET>
					</FORM>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

</xsl:stylesheet>