package io.netstrap.config.tool;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取yml配置文件到map
 * @author minghu.zhang
 */
@Log4j2
public class ConfigReader {
	
	/**
	 * Properties值
	 */
	private Map<String,String> properties = new HashMap<>();
	
	/**
	 * 配置流
	 */
	private InputStream stream;
	/**
	 * 前綴
	 */
	private String      prefix;
	/**
	 * 正則
	 */
	private final static String VAR_REG = "\\$\\{(.*?)\\}";
	/**
	 * 替換
	 */
	private final static Pattern PATTERN  = Pattern.compile(VAR_REG);
	
	/**
	 * 構造Reader對象
	 */
	public ConfigReader(String prefix) {
		super();
		this.prefix = prefix;
	}
	
	/**
	 * 構造Reader對象
	 */
	public ConfigReader() {
		super();
	}

    /**
     * 从输入流读取配置文件
     */
	public Map<String,String> readByInputStream(InputStream stream) {
		this.stream = stream;
		return readYml();
	}

    /**
     * 从类路径读取配置文件
     */
	public Map<String,String> readByClassPath(String path) {
		this.stream = ConfigReader.class.getResourceAsStream(path);
		return readYml();
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> readYml() {
		
		try {
			if(Objects.nonNull(stream)) {
				YamlReader reader = new YamlReader(new InputStreamReader(stream));
				ymlToProperties(((Map<String, ?>)reader.read()), prefix);
				propertiesVariableSet();
			}
		} catch (YamlException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	
	/**
	 * 变量设置
	 */
	private void propertiesVariableSet() {
		for (String key : properties.keySet()) {
			Object value = properties.get(key);
			if(value instanceof String) {
				properties.put(key, stringVariableMatcher(value.toString()));
			}
		}
	}
	
	/**
	 * 替換字符串
	 */
	private String stringVariableMatcher(String value) {
		
		String citeValue = value.toString();
		Matcher matcher = PATTERN.matcher(citeValue);
		
		while(matcher.find()) {
			String variable = matcher.group(1);
			if(properties.containsKey(variable)) {
				String citeResult = properties.get(variable).toString();
				citeValue = citeValue.replace("${"+variable+"}", citeResult);
			}
		}
		
		return citeValue;
	}
	
	/**
	 * yml文件值转properties
	 */
	@SuppressWarnings("unchecked")
	private void ymlToProperties(Map<String,?> values,String prefix) {
		for (String key : values.keySet()) {
			Object value = values.get(key);
			if(value instanceof Map) {
				ymlToProperties((Map<String,?>)value,(!StringUtils.isEmpty(prefix)?prefix+".":"")+key);
			} else {
				properties.put(prefix+"."+key, value.toString());
			}
		}
	}
	
}
