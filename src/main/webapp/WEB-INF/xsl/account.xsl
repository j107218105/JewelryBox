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
			<LINK href="{@contextPath}/STYLE/someone.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/default.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/form.js"/>
			<SCRIPT src="{@contextPath}/register.js"/>
			<TITLE>寶盒計畫 &#187; 個人資料</TITLE>
		</HEAD>
		<BODY>
			<HEADER>
				<DIV class="fL">
					<A href="{@contextPath}/">寶盒計畫</A>
					<SPAN>&#187;</SPAN>
					<A>個人資料</A>
				</DIV>
				<DIV class="fR">
					<A>
						<xsl:value-of select="@name"/>
					</A>
					<SPAN>&#187;</SPAN>
					<A href="{@contextPath}/bye.asp">登出</A>
				</DIV>
			</HEADER>
			<SECTION>
				<DIV class="contentWrapper"/>
			</SECTION>
			<DIV class="contentWrapper">
				<NAV id="navigator">
					<UL id="navParts">
						<LI>
							<DIV class="bold">活動</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/available.asp">未開始</A>
								</LI>
								<LI>
									<A href="{@contextPath}/underway.asp">進行中</A>
								</LI>
								<LI>
									<A href="{@contextPath}/finished.asp">已結束</A>
								</LI>
							</UL>
						</LI>
						<LI>
							<DIV class="bold">撥款</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/withdraw.asp">申請</A>
								</LI>
								<LI>
									<A href="{@contextPath}/statement.asp">歷程</A>
								</LI>
							</UL>
						</LI>
						<LI class="openCart">
							<DIV class="bold">帳戶</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/account.asp">個人資料</A>
								</LI>
								<LI>
									<A href="{@contextPath}/score.asp">點數</A>
								</LI>
							</UL>
						</LI>
					</UL>
				</NAV>
				<MENU id="bashful" type="toolbar">
					<DIV id="shield">個人資料</DIV>
				</MENU>
				<DIV id="pixelLine"/>
				<ARTICLE>
					<DIV>
						<xsl:apply-templates select="form"/>
					</DIV>
				</ARTICLE>
			</DIV>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="post">
			<FIELDSET>
				<LEGEND>有<SPAN class="must"></SPAN>者為必填或必選欄位</LEGEND>
				<TABLE class="fieldset">
					<CAPTION>
						<xsl:value-of select="errorMessage"/>
					</CAPTION>
					<TR>
						<TH class="must">
							<LABEL for="birthday">生日</LABEL>
						</TH>
						<TD>
							<INPUT class="dP bitstreamVeraSansMono" id="birthday" disabled="" name="birthday" type="text" value="{birthday}"/>
						</TD>
						<TD class="note">不可變更。</TD>
					</TR>
					<TR>
						<TH class="must">性別</TH>
						<TD>
							<LABEL>
								<INPUT disabled="" name="gender" type="radio" value="false">
									<xsl:if test="gender='false'">
										<xsl:attribute name="checked"/>
									</xsl:if>
								</INPUT>
								<SPAN>女性</SPAN>
							</LABEL>
							<LABEL>
								<INPUT disabled="" name="gender" type="radio" value="true">
									<xsl:if test="gender='true'">
										<xsl:attribute name="checked"/>
									</xsl:if>
								</INPUT>
								<SPAN>男性</SPAN>
							</LABEL>
						</TD>
						<TD class="note">不可變更。</TD>
					</TR>
					<TR>
						<TH class="must">
							<LABEL for="name">真實姓名</LABEL>
						</TH>
						<TD>
							<INPUT id="name" name="name" type="text" value="{name}"/>
						</TD>
						<TD class="note">必填。</TD>
					</TR>
					<TR>
						<TH class="must">
							<LABEL for="email">電子郵件</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="email" name="email" type="text" value="{email}"/>
						</TD>
						<TD class="note">必填且不得與現有會員重複。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="cellular">手機</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="cellular" maxlength="10" name="cellular" type="text" value="{cellular}"/>
						</TD>
						<TD class="note">可變更但不得與現有會員重複；未填寫將會導致您無法接受活動通知簡訊。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="occupation">職業</LABEL>
						</TH>
						<TD>
							<SELECT id="occupation" name="occupation">
								<OPTION value="">(職業)</OPTION>
								<xsl:apply-templates select="occupation/*"/>
							</SELECT>
						</TD>
						<TD class="note">可稍後補選；未選擇可能導致您無法符合活動參加資格。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="district">行政區</LABEL>
						</TH>
						<TD>
							<SELECT id="district" name="district">
								<OPTION value="">(行政區)</OPTION>
								<xsl:apply-templates select="district/*"/>
							</SELECT>
						</TD>
						<TD class="note">可重新選擇；未選擇可能導致您無法符合活動參加資格。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialInstitution">金融機構</LABEL>
						</TH>
						<TD>
							<SELECT id="financialInstitution" name="financialInstitution">
								<OPTION value="">(金融機構)</OPTION>
								<xsl:apply-templates select="financialInstitution/*"/>
							</SELECT>
						</TD>
						<TD class="note">可重新選擇；未選擇將會導致您無法申請撥款。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialAccountHolder">金融帳戶戶名</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="financialAccountHolder" name="financialAccountHolder" type="text" value="{financialAccountHolder}">
								<xsl:if test="not(financialInstitution/*/@selected)">
									<xsl:attribute name="disabled"/>
								</xsl:if>
							</INPUT>
						</TD>
						<TD class="note">可重新填寫；未填寫將會導致您無法申請撥款。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialAccountNumber">金融帳戶號碼</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="financialAccountNumber" name="financialAccountNumber" type="text" value="{financialAccountNumber}">
								<xsl:if test="not(financialInstitution/*/@selected)">
									<xsl:attribute name="disabled"/>
								</xsl:if>
							</INPUT>
						</TD>
						<TD class="note">可重新填寫；未填寫將會導致您無法申請撥款。</TD>
					</TR>
				</TABLE>
				<DIV class="cF">
					<INPUT class="fL" type="reset" value="取消"/>
					<INPUT class="fR" type="submit" value="確定"/>
				</DIV>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>